package hu.cinema.model;

import java.io.Serializable;

/** The person who makes a booking. */
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final String email;

    public Customer(String name, String email) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Customer name must not be empty");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email must contain '@'");
        }
        this.name = name;
        this.email = email;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }

    @Override
    public String toString() { return name + " <" + email + ">"; }
}
