package hu.cinema.persistence;

import hu.cinema.model.Booking;
import hu.cinema.model.Movie;
import hu.cinema.model.Screening;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** Everything that gets saved to disk, bundled in one object. */
public class CinemaData implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Movie> movies;
    private final List<Screening> screenings;
    private final List<Booking> bookings;

    public CinemaData(List<Movie> movies, List<Screening> screenings, List<Booking> bookings) {
        this.movies = new ArrayList<>(movies);
        this.screenings = new ArrayList<>(screenings);
        this.bookings = new ArrayList<>(bookings);
    }

    public List<Movie> getMovies() { return movies; }
    public List<Screening> getScreenings() { return screenings; }
    public List<Booking> getBookings() { return bookings; }
}
