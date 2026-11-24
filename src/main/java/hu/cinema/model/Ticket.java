package hu.cinema.model;

import java.io.Serializable;

/**
 * Abstract parent of all ticket types. Each subclass overrides getPrice(),
 * so the price is decided at runtime (polymorphism).
 */
public abstract class Ticket implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Seat seat;

    protected Ticket(Seat seat) {
        if (seat == null) {
            throw new IllegalArgumentException("Ticket needs a seat");
        }
        this.seat = seat;
    }

    public Seat getSeat() { return seat; }

    /** Price of this ticket, calculated from the screening's base price. */
    public abstract double getPrice(double basePrice);

    /** Short name of the ticket type, e.g. "Student". */
    public abstract String getTypeName();

    @Override
    public String toString() { return getTypeName() + " ticket, seat " + seat; }
}
