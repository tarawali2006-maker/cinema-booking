package hu.cinema;

import hu.cinema.model.Customer;
import hu.cinema.model.Hall;
import hu.cinema.model.Movie;
import hu.cinema.model.RegularTicket;
import hu.cinema.model.Screening;
import hu.cinema.persistence.CinemaData;
import hu.cinema.persistence.FileCinemaRepository;
import hu.cinema.service.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersistenceTest {

    @TempDir
    Path tempDir;

    @Test
    void savedDataCanBeLoadedBack() throws Exception {
        BookingService service = new BookingService();
        Movie movie = new Movie("Saved Movie", "Comedy", 90);
        service.addMovie(movie);
        Screening s = new Screening(1, movie, new Hall("Hall 1", 2, 2), LocalDateTime.now().plusDays(1), 1500);
        service.addScreening(s);
        service.book(new Customer("Bela", "bela@example.com"), s, List.of("A1"), RegularTicket::new);

        FileCinemaRepository repo = new FileCinemaRepository(tempDir.resolve("test.dat"));
        repo.save(service.toData());

        CinemaData loaded = repo.load();
        assertNotNull(loaded);
        assertEquals(1, loaded.getMovies().size());
        assertEquals(1, loaded.getBookings().size());
        assertEquals(3, loaded.getScreenings().get(0).getFreeSeatCount());
    }

    @Test
    void loadReturnsNullWhenNoFile() throws Exception {
        FileCinemaRepository repo = new FileCinemaRepository(tempDir.resolve("missing.dat"));
        assertNull(repo.load());
    }
}
