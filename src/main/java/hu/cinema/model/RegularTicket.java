package hu.cinema.model;

/** Full price ticket. */
public class RegularTicket extends Ticket {
    private static final long serialVersionUID = 1L;

    public RegularTicket(Seat seat) { super(seat); }

    @Override
    public double getPrice(double basePrice) { return basePrice; }

    @Override
    public String getTypeName() { return "Regular"; }
}
