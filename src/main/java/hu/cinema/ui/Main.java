package hu.cinema.ui;

import hu.cinema.model.Hall;
import hu.cinema.model.Movie;
import hu.cinema.model.Screening;
import hu.cinema.persistence.CinemaData;
import hu.cinema.persistence.CinemaRepository;
import hu.cinema.persistence.FileCinemaRepository;
import hu.cinema.service.BookingService;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

/** Entry point: loads data, starts the menu. */
public class Main {
    public static void main(String[] args) {
        CinemaRepository repository = new FileCinemaRepository(Path.of("cinema.dat"));
        BookingService service;

        try {
            CinemaData data = repository.load();
            if (data == null) {
                System.out.println("No saved data found, creating sample data.");
                service = createSampleData();
            } else {
                System.out.println("Loaded saved data.");
                service = new BookingService(data);
            }
        } catch (IOException e) {
            System.out.println("Could not load data (" + e.getMessage() + "), starting with sample data.");
            service = createSampleData();
        }

        new ConsoleMenu(service, repository, new Scanner(System.in)).run();
    }

    static BookingService createSampleData() {
        BookingService service = new BookingService();

        Movie dune = new Movie("Dune: Part Two", "Sci-Fi", 166);
        Movie inside = new Movie("Inside Out 2", "Animation", 96);
        Movie oppen = new Movie("Oppenheimer", "Drama", 180);
        service.addMovie(dune);
        service.addMovie(inside);
        service.addMovie(oppen);

        Hall hall1 = new Hall("Hall 1", 5, 8);
        Hall hall2 = new Hall("Hall 2", 4, 6);

        LocalDate today = LocalDate.now();
        service.addScreening(new Screening(1, dune, hall1, LocalDateTime.of(today.plusDays(1), LocalTime.of(20, 0)), 2800));
        service.addScreening(new Screening(2, inside, hall2, LocalDateTime.of(today.plusDays(1), LocalTime.of(15, 30)), 2200));
        service.addScreening(new Screening(3, oppen, hall1, LocalDateTime.of(today.plusDays(2), LocalTime.of(18, 0)), 2800));
        return service;
    }
}
