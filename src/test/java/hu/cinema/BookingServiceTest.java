package hu.cinema;

import hu.cinema.exception.InvalidSeatException;
import hu.cinema.exception.SeatAlreadyBookedException;
import hu.cinema.model.Booking;
import hu.cinema.model.Customer;
import hu.cinema.model.Hall;
import hu.cinema.model.Movie;
import hu.cinema.model.RegularTicket;
import hu.cinema.model.Screening;
import hu.cinema.model.StudentTicket;
import hu.cinema.pricing.DiscountDayPricing;
import hu.cinema.pricing.WeekendPricing;
import hu.cinema.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingServiceTest {
    private BookingService service;
    private Screening saturdayScreening;
    private Customer customer;

    @BeforeEach
    void setUp() {
        service = new BookingService();
        Movie movie = new Movie("Test Movie", "Drama", 100);
        service.addMovie(movie);
        // 2026-11-28 is a Saturday
        saturdayScreening = new Screening(1, movie, new Hall("Hall 1", 3, 4),
                LocalDateTime.of(2026, 11, 28, 20, 0), 2000);
        service.addScreening(saturdayScreening);
        customer = new Customer("Anna", "anna@example.com");
    }

    @Test
    void bookingASeatMarksItAsTaken() throws Exception {
        Booking b = service.book(customer, saturdayScreening, List.of("A1"), RegularTicket::new);
        assertEquals(1, b.getTickets().size());
        assertEquals(11, saturdayScreening.getFreeSeatCount());
        assertEquals(2000, b.getTotalPrice(), 0.001);
    }

    @Test
    void doubleBookingThrowsException() throws Exception {
        service.book(customer, saturdayScreening, List.of("A1"), RegularTicket::new);
        assertThrows(SeatAlreadyBookedException.class,
                () -> service.book(customer, saturdayScreening, List.of("A1"), RegularTicket::new));
    }

    @Test
    void invalidSeatThrowsAndBooksNothing() {
        assertThrows(InvalidSeatException.class,
                () -> service.book(customer, saturdayScreening, List.of("A1", "Z99"), RegularTicket::new));
        // A1 must have been released again (all-or-nothing)
        assertEquals(12, saturdayScreening.getFreeSeatCount());
    }

    @Test
    void studentTicketIsCheaper() throws Exception {
        Booking b = service.book(customer, saturdayScreening, List.of("B1"), StudentTicket::new);
        assertEquals(1400, b.getTotalPrice(), 0.001);
    }

    @Test
    void strategyChangesPriceAtRuntime() throws Exception {
        service.setPricingStrategy(new WeekendPricing());
        Booking weekend = service.book(customer, saturdayScreening, List.of("C1"), RegularTicket::new);
        assertEquals(2400, weekend.getTotalPrice(), 0.001);

        service.setPricingStrategy(new DiscountDayPricing()); // Saturday is not Tuesday -> no discount
        Booking normal = service.book(customer, saturdayScreening, List.of("C2"), RegularTicket::new);
        assertEquals(2000, normal.getTotalPrice(), 0.001);
    }

    @Test
    void cancelReleasesSeats() throws Exception {
        Booking b = service.book(customer, saturdayScreening, List.of("A1", "A2"), RegularTicket::new);
        assertTrue(service.cancelBooking(b.getId()));
        assertEquals(12, saturdayScreening.getFreeSeatCount());
    }
}
