package hu.cinema.model;

import java.io.Serializable;
import java.util.Objects;

/** A film the cinema shows. Sorted by title (Comparable). */
public class Movie implements Comparable<Movie>, Serializable {
    private static final long serialVersionUID = 1L;

    private final String title;
    private final String genre;
    private final int durationMinutes;

    public Movie(String title, String genre, int durationMinutes) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be empty");
        }
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }
        this.title = title;
        this.genre = genre == null ? "Unknown" : genre;
        this.durationMinutes = durationMinutes;
    }

    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public int getDurationMinutes() { return durationMinutes; }

    @Override
    public int compareTo(Movie other) {
        return this.title.compareToIgnoreCase(other.title);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        return title.equalsIgnoreCase(((Movie) o).title);
    }

    @Override
    public int hashCode() { return Objects.hash(title.toLowerCase()); }

    @Override
    public String toString() {
        return title + " (" + genre + ", " + durationMinutes + " min)";
    }
}
