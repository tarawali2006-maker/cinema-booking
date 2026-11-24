package hu.cinema.model;

/** 30% cheaper than a regular ticket. */
public class StudentTicket extends Ticket {
    private static final long serialVersionUID = 1L;

    public StudentTicket(Seat seat) { super(seat); }

    @Override
    public double getPrice(double basePrice) { return basePrice * 0.7; }

    @Override
    public String getTypeName() { return "Student"; }
}
