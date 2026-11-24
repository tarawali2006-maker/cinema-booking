package hu.cinema.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A customer's booking for one screening.
 * Aggregation: 1 Booking -> 1..* Tickets, 1 Booking -> 1 Customer, 1 Booking -> 1 Screening.
 */
public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int id;
    private final Customer customer;
    private final Screening screening;
    private final List<Ticket> tickets;
    private final double totalPrice;

    public Booking(int id, Customer customer, Screening screening, List<Ticket> tickets, double totalPrice) {
        if (customer == null || screening == null) {
            throw new IllegalArgumentException("Customer and screening are required");
        }
        if (tickets == null || tickets.isEmpty()) {
            throw new IllegalArgumentException("A booking needs at least one ticket");
        }
        this.id = id;
        this.customer = customer;
        this.screening = screening;
        this.tickets = new ArrayList<>(tickets);
        this.totalPrice = totalPrice;
    }

    public int getId() { return id; }
    public Customer getCustomer() { return customer; }
    public Screening getScreening() { return screening; }
    public List<Ticket> getTickets() { return Collections.unmodifiableList(tickets); }
    public double getTotalPrice() { return totalPrice; }

    @Override
    public String toString() {
        return String.format("Booking #%d | %s | %s | %s | total %.0f Ft",
                id, customer.getName(), screening.getMovie().getTitle(), tickets, totalPrice);
    }
}
