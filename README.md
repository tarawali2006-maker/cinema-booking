# Cinema Booking System

A Java console application for a small cinema. Cashiers can list movies and screenings, view a seat map,
book seats with different ticket types, cancel bookings and switch pricing rules. All data is saved between runs.

## Problem statement
**Users:** cinema cashiers and the cinema manager.
**Goal:** sell tickets quickly and correctly.
**Problem solved:** without a system, seats can be sold twice, prices are calculated by hand, and bookings are lost.
This app prevents double booking, calculates prices automatically, and saves everything to a file.

## Prerequisites
- Java 17 or newer (`java -version`)
- Maven 3.8+ (`mvn -version`)

## Build, run, test
```bash
mvn clean package        # compile + run tests + build the jar
mvn exec:java            # run the app
# or
java -jar target/cinema-booking-1.0.0.jar
mvn test                 # run only the tests
```
On the first start, sample movies and screenings are created. Data is saved to `cinema.dat` when you choose `0) Save and exit`.

## Sample usage
```
===== CINEMA BOOKING =====  (pricing: Standard)
1) List movies
2) List screenings
3) Show seat map
4) Book seats
...
Choose: 4
Screening id: 1
        SCREEN
A [ ][ ][ ][ ][ ][ ][ ][ ]
...
Customer name: Anna
Customer email: anna@example.com
Seats (e.g. A1,A2): A1,A2
Ticket type: 1) Regular  2) Student  3) VIP
Choose: 2
Booked! Booking #1 | Anna | Dune: Part Two | [Student ticket, seat A1, Student ticket, seat A2] | total 3920 Ft
```

## Implemented requirements
| Requirement | Where |
|---|---|
| Encapsulation | All fields `private`, validation in constructors, collections returned as unmodifiable views (`Hall.getSeats()`, `Booking.getTickets()`) |
| Abstraction | Abstract class `Ticket`; interfaces `PricingStrategy`, `CinemaRepository` |
| Inheritance & polymorphism | `RegularTicket`, `StudentTicket`, `VipTicket` override `getPrice()`; `BookingService.book()` calls it on `Ticket` without knowing the type |
| Relationships | `Hall` 1 ◆— 1..* `Seat` (composition); `Screening` * → 1 `Movie`, * → 1 `Hall`; `Booking` 1 ◇— 1..* `Ticket`, * → 1 `Customer` |
| Collections & generics | `List<Movie>`, `List<Screening>`, `Set<Seat>`, `Optional<Screening>`, `Function<Seat, Ticket>` |
| Sorting | `Movie implements Comparable<Movie>` (by title); lambda comparator for screenings by time; `Comparator.comparingInt(...).reversed()` by free seats |
| Exceptions | Custom checked exceptions `SeatAlreadyBookedException`, `InvalidSeatException`; input validation in `ConsoleMenu.readInt()` |
| Persistence | Java serialization in `FileCinemaRepository` using try-with-resources |
| Tests | JUnit 5: `BookingServiceTest`, `PersistenceTest` |
| Build | Maven, standard directory layout |

## Design pattern: Strategy
**Problem:** the cinema wants different pricing rules (normal, weekend surcharge, cheap Tuesday) and may add more later.
Putting `if/else` for every rule inside `BookingService` would make it grow and break every time a rule changes.

**Participants:**
- *Strategy interface:* `PricingStrategy`
- *Concrete strategies:* `StandardPricing`, `WeekendPricing`, `DiscountDayPricing`
- *Context:* `BookingService` (holds a `PricingStrategy` field and calls `calculatePrice()`)
- *Client:* `ConsoleMenu` (menu option 7 calls `setPricingStrategy(...)`)

**Benefit:** the rule can be switched at runtime and a new rule is just a new class - `BookingService` does not change (Open/Closed principle).
See `docs/uml.png` for the diagram.

## Design decisions
- Packages separate concerns: `model`, `service`, `persistence`, `ui`, `pricing`, `exception`.
- Booking several seats is all-or-nothing: if one seat fails, the already reserved seats are released.
- The ticket type is passed as a `Function<Seat, Ticket>` (e.g. `StudentTicket::new`), so the service doesn't need a switch on ticket types.
- `CinemaRepository` is an interface so the storage could later be changed to JSON or a database.

## Known limitations
- The active pricing rule is not saved; it is Standard after a restart.
- Prices are stored as `double` (fine for this project; real money should use `BigDecimal`).
- Only one user at a time; no login.
- Movies and screenings can't be added from the menu (sample data is created at first start).
- If a class changes, an old `cinema.dat` may not load - the app then starts with sample data.

## Documentation
- `docs/uml.png` / `docs/uml.svg` – class diagram (image)
- `docs/uml.dot` – editable source of the image (Graphviz: `dot -Tpng docs/uml.dot -o docs/uml.png`)
- `docs/uml.puml` – the same diagram as PlantUML source
- `docs/user-stories.md` – user stories (match GitHub Issues #1–#6)

## Contributors
- Ivan Tarawali (Neptun: I71BMG)
