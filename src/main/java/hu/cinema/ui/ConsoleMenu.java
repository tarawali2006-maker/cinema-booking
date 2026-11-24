package hu.cinema.ui;

import hu.cinema.exception.InvalidSeatException;
import hu.cinema.exception.SeatAlreadyBookedException;
import hu.cinema.model.Booking;
import hu.cinema.model.Customer;
import hu.cinema.model.Movie;
import hu.cinema.model.RegularTicket;
import hu.cinema.model.Screening;
import hu.cinema.model.Seat;
import hu.cinema.model.StudentTicket;
import hu.cinema.model.Ticket;
import hu.cinema.model.VipTicket;
import hu.cinema.persistence.CinemaRepository;
import hu.cinema.pricing.DiscountDayPricing;
import hu.cinema.pricing.StandardPricing;
import hu.cinema.pricing.WeekendPricing;
import hu.cinema.service.BookingService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

/** Console user interface. Reads input, calls BookingService, prints results. */
public class ConsoleMenu {
    private final BookingService service;
    private final CinemaRepository repository;
    private final Scanner scanner;

    public ConsoleMenu(BookingService service, CinemaRepository repository, Scanner scanner) {
        this.service = service;
        this.repository = repository;
        this.scanner = scanner;
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose: ");
            switch (choice) {
                case 1 -> listMovies();
                case 2 -> listScreenings();
                case 3 -> showSeatMap();
                case 4 -> bookSeats();
                case 5 -> listBookings();
                case 6 -> cancelBooking();
                case 7 -> changePricing();
                case 0 -> running = false;
                default -> System.out.println("Unknown option, try again.");
            }
        }
        save();
        System.out.println("Goodbye!");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("===== CINEMA BOOKING =====  (pricing: " + service.getPricingStrategy().getName() + ")");
        System.out.println("1) List movies");
        System.out.println("2) List screenings");
        System.out.println("3) Show seat map");
        System.out.println("4) Book seats");
        System.out.println("5) List bookings");
        System.out.println("6) Cancel booking");
        System.out.println("7) Change pricing rule");
        System.out.println("0) Save and exit");
    }

    private void listMovies() {
        List<Movie> movies = service.getMoviesSorted();
        if (movies.isEmpty()) {
            System.out.println("No movies.");
        }
        movies.forEach(m -> System.out.println(" - " + m));
    }

    private void listScreenings() {
        System.out.println("Sort by: 1) time  2) free seats");
        int sort = readInt("Choose: ");
        List<Screening> list = sort == 2 ? service.getScreeningsByFreeSeats() : service.getScreeningsSortedByTime();
        list.forEach(s -> System.out.println(" " + s));
    }

    private Optional<Screening> askScreening() {
        listScreeningsShort();
        int id = readInt("Screening id: ");
        Optional<Screening> s = service.findScreening(id);
        if (s.isEmpty()) {
            System.out.println("No screening with id " + id);
        }
        return s;
    }

    private void listScreeningsShort() {
        service.getScreeningsSortedByTime().forEach(s -> System.out.println(" " + s));
    }

    private void showSeatMap() {
        askScreening().ifPresent(s -> System.out.print(s.seatMap()));
    }

    private void bookSeats() {
        Optional<Screening> maybe = askScreening();
        if (maybe.isEmpty()) {
            return;
        }
        Screening screening = maybe.get();
        System.out.print(screening.seatMap());

        try {
            String name = readLine("Customer name: ");
            String email = readLine("Customer email: ");
            Customer customer = new Customer(name, email);

            String seatsText = readLine("Seats (e.g. A1,A2): ");
            List<String> seats = Arrays.asList(seatsText.split(","));

            System.out.println("Ticket type: 1) Regular  2) Student  3) VIP");
            int type = readInt("Choose: ");
            Function<Seat, Ticket> maker = switch (type) {
                case 2 -> StudentTicket::new;
                case 3 -> VipTicket::new;
                default -> RegularTicket::new;
            };

            Booking booking = service.book(customer, screening, seats, maker);
            System.out.println("Booked! " + booking);
        } catch (SeatAlreadyBookedException | InvalidSeatException e) {
            System.out.println("Booking failed: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    private void listBookings() {
        if (service.getBookings().isEmpty()) {
            System.out.println("No bookings yet.");
        }
        service.getBookings().forEach(b -> System.out.println(" " + b));
    }

    private void cancelBooking() {
        listBookings();
        int id = readInt("Booking id to cancel: ");
        System.out.println(service.cancelBooking(id) ? "Cancelled." : "No booking with id " + id);
    }

    private void changePricing() {
        System.out.println("1) Standard  2) Weekend (+20%)  3) Cheap Tuesday (-50%)");
        int c = readInt("Choose: ");
        switch (c) {
            case 2 -> service.setPricingStrategy(new WeekendPricing());
            case 3 -> service.setPricingStrategy(new DiscountDayPricing());
            default -> service.setPricingStrategy(new StandardPricing());
        }
        System.out.println("Pricing is now: " + service.getPricingStrategy().getName());
    }

    private void save() {
        try {
            repository.save(service.toData());
            System.out.println("Data saved.");
        } catch (IOException e) {
            System.out.println("Could not save data: " + e.getMessage());
        }
    }

    // ---------- input helpers ----------
    private String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            return "0";
        }
        return scanner.nextLine().trim();
    }

    /** Keeps asking until the user types a number. */
    private int readInt(String prompt) {
        while (true) {
            String line = readLine(prompt);
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please type a number.");
            }
        }
    }
}
