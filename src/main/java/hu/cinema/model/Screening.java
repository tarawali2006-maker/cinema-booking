package hu.cinema.model;

import hu.cinema.exception.InvalidSeatException;
import hu.cinema.exception.SeatAlreadyBookedException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * One showing of a movie in a hall at a given time.
 * Association: 1 Screening -> 1 Movie, 1 Screening -> 1 Hall.
 */
public class Screening implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final int id;
    private final Movie movie;
    private final Hall hall;
    private final LocalDateTime startTime;
    private final double basePrice;
    private final Set<Seat> bookedSeats = new HashSet<>();

    public Screening(int id, Movie movie, Hall hall, LocalDateTime startTime, double basePrice) {
        if (movie == null || hall == null || startTime == null) {
            throw new IllegalArgumentException("Movie, hall and start time are required");
        }
        if (basePrice <= 0) {
            throw new IllegalArgumentException("Base price must be positive");
        }
        this.id = id;
        this.movie = movie;
        this.hall = hall;
        this.startTime = startTime;
        this.basePrice = basePrice;
    }

    public int getId() { return id; }
    public Movie getMovie() { return movie; }
    public Hall getHall() { return hall; }
    public LocalDateTime getStartTime() { return startTime; }
    public double getBasePrice() { return basePrice; }

    public boolean isBooked(Seat seat) { return bookedSeats.contains(seat); }

    public int getFreeSeatCount() { return hall.getCapacity() - bookedSeats.size(); }

    public Set<Seat> getBookedSeats() { return Collections.unmodifiableSet(bookedSeats); }

    /** Marks a seat as taken. This is where the "no double booking" rule lives. */
    public void reserve(Seat seat) throws SeatAlreadyBookedException, InvalidSeatException {
        if (!hall.getSeats().contains(seat)) {
            throw new InvalidSeatException("Seat " + seat + " is not in " + hall.getName());
        }
        if (bookedSeats.contains(seat)) {
            throw new SeatAlreadyBookedException(seat.getCode());
        }
        bookedSeats.add(seat);
    }

    public void release(Seat seat) { bookedSeats.remove(seat); }

    /** Prints the seat map: [ ] free, [X] booked. */
    public String seatMap() {
        StringBuilder sb = new StringBuilder("        SCREEN\n");
        for (int r = 0; r < hall.getRows(); r++) {
            char row = (char) ('A' + r);
            sb.append(row).append(' ');
            for (int n = 1; n <= hall.getSeatsPerRow(); n++) {
                sb.append(isBooked(new Seat(row, n)) ? "[X]" : "[ ]");
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("#%d %s | %s | %s | %.0f Ft | %d free",
                id, movie.getTitle(), hall.getName(), startTime.format(FORMAT), basePrice, getFreeSeatCount());
    }
}
