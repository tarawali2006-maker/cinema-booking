package hu.cinema.exception;

/** Checked exception: the seat code does not exist in the hall (e.g. "Z99"). */
public class InvalidSeatException extends Exception {
    public InvalidSeatException(String message) {
        super(message);
    }
}
