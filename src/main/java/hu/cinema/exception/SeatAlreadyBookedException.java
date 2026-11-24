package hu.cinema.exception;

/** Checked exception for the domain rule "a seat can only be booked once per screening". */
public class SeatAlreadyBookedException extends Exception {
    private final String seatCode;

    public SeatAlreadyBookedException(String seatCode) {
        super("Seat " + seatCode + " is already booked for this screening");
        this.seatCode = seatCode;
    }

    public String getSeatCode() { return seatCode; }
}
