package hu.cinema.model;

import hu.cinema.exception.InvalidSeatException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A cinema hall. Composition: the hall creates and owns its seats (1 Hall -> many Seats).
 */
public class Hall implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final int rows;
    private final int seatsPerRow;
    private final List<Seat> seats = new ArrayList<>();

    public Hall(String name, int rows, int seatsPerRow) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Hall name must not be empty");
        }
        if (rows < 1 || rows > 26 || seatsPerRow < 1) {
            throw new IllegalArgumentException("Rows must be 1-26 and seats per row at least 1");
        }
        this.name = name;
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
        for (int r = 0; r < rows; r++) {
            for (int n = 1; n <= seatsPerRow; n++) {
                seats.add(new Seat((char) ('A' + r), n));
            }
        }
    }

    public String getName() { return name; }
    public int getRows() { return rows; }
    public int getSeatsPerRow() { return seatsPerRow; }
    public int getCapacity() { return seats.size(); }

    /** Read-only view so nobody outside can add or remove seats. */
    public List<Seat> getSeats() { return Collections.unmodifiableList(seats); }

    /** Finds a seat by its code, e.g. "C7". */
    public Seat findSeat(String code) throws InvalidSeatException {
        if (code == null || code.length() < 2) {
            throw new InvalidSeatException("Invalid seat code: " + code);
        }
        char row = Character.toUpperCase(code.charAt(0));
        int number;
        try {
            number = Integer.parseInt(code.substring(1).trim());
        } catch (NumberFormatException e) {
            throw new InvalidSeatException("Invalid seat code: " + code);
        }
        for (Seat s : seats) {
            if (s.getRow() == row && s.getNumber() == number) {
                return s;
            }
        }
        throw new InvalidSeatException("Seat " + code + " does not exist in " + name);
    }

    @Override
    public String toString() {
        return name + " (" + rows + " rows x " + seatsPerRow + " seats)";
    }
}
