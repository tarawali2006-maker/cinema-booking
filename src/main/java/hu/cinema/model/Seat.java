package hu.cinema.model;

import java.io.Serializable;
import java.util.Objects;

/** One seat in a hall, e.g. row 'B', number 5 = "B5". */
public class Seat implements Serializable {
    private static final long serialVersionUID = 1L;

    private final char row;
    private final int number;

    public Seat(char row, int number) {
        if (!Character.isLetter(row)) {
            throw new IllegalArgumentException("Row must be a letter");
        }
        if (number <= 0) {
            throw new IllegalArgumentException("Seat number must be positive");
        }
        this.row = Character.toUpperCase(row);
        this.number = number;
    }

    public char getRow() { return row; }
    public int getNumber() { return number; }

    /** Seat code like "B5". */
    public String getCode() { return "" + row + number; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seat)) return false;
        Seat s = (Seat) o;
        return row == s.row && number == s.number;
    }

    @Override
    public int hashCode() { return Objects.hash(row, number); }

    @Override
    public String toString() { return getCode(); }
}
