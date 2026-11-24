package hu.cinema.persistence;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/** Saves all data with Java serialization into one file, using try-with-resources. */
public class FileCinemaRepository implements CinemaRepository {
    private final Path file;

    public FileCinemaRepository(Path file) {
        this.file = file;
    }

    @Override
    public void save(CinemaData data) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file.toFile()))) {
            out.writeObject(data);
        }
    }

    @Override
    public CinemaData load() throws IOException {
        if (!Files.exists(file)) {
            return null;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file.toFile()))) {
            return (CinemaData) in.readObject();
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new IOException("Data file is corrupted or from an old version", e);
        }
    }
}
