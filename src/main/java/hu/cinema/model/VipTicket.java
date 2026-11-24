package hu.cinema.model;

/** 50% more expensive, includes popcorn. */
public class VipTicket extends Ticket {
    private static final long serialVersionUID = 1L;

    public VipTicket(Seat seat) { super(seat); }

    @Override
    public double getPrice(double basePrice) { return basePrice * 1.5; }

    @Override
    public String getTypeName() { return "VIP"; }
}
