package hu.cinema.persistence;

import java.io.IOException;

/** Abstraction for saving and loading. The service does not care if it is a file, JSON or database. */
public interface CinemaRepository {
    void save(CinemaData data) throws IOException;

    /** Returns null if there is no saved data yet. */
    CinemaData load() throws IOException;
}
