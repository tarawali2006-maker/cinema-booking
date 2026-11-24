package hu.cinema.service;

import hu.cinema.exception.InvalidSeatException;
import hu.cinema.exception.SeatAlreadyBookedException;
import hu.cinema.model.Booking;
import hu.cinema.model.Customer;
import hu.cinema.model.Movie;
import hu.cinema.model.Screening;
import hu.cinema.model.Seat;
import hu.cinema.model.Ticket;
import hu.cinema.persistence.CinemaData;
import hu.cinema.pricing.PricingStrategy;
import hu.cinema.pricing.StandardPricing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/** Business logic of the cinema. The UI only talks to this class. */
public class BookingService {
    private final List<Movie> movies = new ArrayList<>();
    private final List<Screening> screenings = new ArrayList<>();
    private final List<Booking> bookings = new ArrayList<>();
    private PricingStrategy pricingStrategy = new StandardPricing();

    public BookingService() { }

    public BookingService(CinemaData data) {
        if (data != null) {
            movies.addAll(data.getMovies());
            screenings.addAll(data.getScreenings());
            bookings.addAll(data.getBookings());
        }
    }

    // ---------- Strategy ----------
    public void setPricingStrategy(PricingStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy must not be null");
        }
        this.pricingStrategy = strategy;
    }

    public PricingStrategy getPricingStrategy() { return pricingStrategy; }

    // ---------- Movies & screenings ----------
    public void addMovie(Movie movie) {
        if (movies.contains(movie)) {
            throw new IllegalArgumentException("Movie already exists: " + movie.getTitle());
        }
        movies.add(movie);
    }

    public void addScreening(Screening screening) { screenings.add(screening); }

    /** Movies sorted by title, using Movie's compareTo (Comparable). */
    public List<Movie> getMoviesSorted() {
        List<Movie> copy = new ArrayList<>(movies);
        Collections.sort(copy);
        return copy;
    }

    /** Screenings sorted by start time, using a lambda Comparator. */
    public List<Screening> getScreeningsSortedByTime() {
        List<Screening> copy = new ArrayList<>(screenings);
        copy.sort((a, b) -> a.getStartTime().compareTo(b.getStartTime()));
        return copy;
    }

    /** Screenings with the most free seats first. */
    public List<Screening> getScreeningsByFreeSeats() {
        List<Screening> copy = new ArrayList<>(screenings);
        copy.sort(Comparator.comparingInt(Screening::getFreeSeatCount).reversed());
        return copy;
    }

    public Optional<Screening> findScreening(int id) {
        return screenings.stream().filter(s -> s.getId() == id).findFirst();
    }

    public int nextScreeningId() {
        return screenings.stream().mapToInt(Screening::getId).max().orElse(0) + 1;
    }

    // ---------- Booking ----------
    /**
     * Books one or more seats. If any seat fails, nothing is booked (all-or-nothing).
     *
     * @param ticketMaker turns a Seat into a Ticket (e.g. StudentTicket::new)
     */
    public Booking book(Customer customer, Screening screening, List<String> seatCodes,
                        Function<Seat, Ticket> ticketMaker)
            throws SeatAlreadyBookedException, InvalidSeatException {
        if (seatCodes == null || seatCodes.isEmpty()) {
            throw new IllegalArgumentException("Choose at least one seat");
        }
        List<Seat> reserved = new ArrayList<>();
        try {
            for (String code : seatCodes) {
                Seat seat = screening.getHall().findSeat(code.trim());
                screening.reserve(seat);
                reserved.add(seat);
            }
        } catch (SeatAlreadyBookedException | InvalidSeatException e) {
            reserved.forEach(screening::release); // undo the seats we already took
            throw e;
        }

        List<Ticket> tickets = new ArrayList<>();
        double total = 0;
        for (Seat seat : reserved) {
            Ticket t = ticketMaker.apply(seat);
            tickets.add(t);
            total += pricingStrategy.calculatePrice(t, screening); // polymorphism + strategy
        }
        int id = bookings.stream().mapToInt(Booking::getId).max().orElse(0) + 1;
        Booking booking = new Booking(id, customer, screening, tickets, total);
        bookings.add(booking);
        return booking;
    }

    public boolean cancelBooking(int bookingId) {
        Optional<Booking> found = bookings.stream().filter(b -> b.getId() == bookingId).findFirst();
        if (found.isEmpty()) {
            return false;
        }
        Booking b = found.get();
        for (Ticket t : b.getTickets()) {
            b.getScreening().release(t.getSeat());
        }
        bookings.remove(b);
        return true;
    }

    public List<Booking> getBookings() { return Collections.unmodifiableList(bookings); }

    /** Bundles everything for saving. */
    public CinemaData toData() {
        return new CinemaData(movies, screenings, bookings);
    }
}
