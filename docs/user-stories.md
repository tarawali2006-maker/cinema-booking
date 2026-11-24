# User Stories

These match the GitHub Issues #1–#6.

## #1 List movies and screenings
As a cashier, I want to see all movies and screenings so that I can tell customers what is playing and when.
- Movies are listed sorted by title
- Screenings can be sorted by start time or by number of free seats
- Each screening shows movie, hall, date/time, price and free seats

## #2 Show seat map for a screening
As a cashier, I want to see which seats are free for a screening so that the customer can choose a seat.
- Seat map shows rows (A, B, C...) and seat numbers
- Free seats are shown as [ ], booked seats as [X]
- A screening id that doesn't exist shows an error message

## #3 Book seats with a ticket type
As a cashier, I want to book one or more seats with a ticket type (Regular, Student, VIP) so that the customer pays the correct price.
- Several seats can be booked at once (e.g. A1,A2)
- Student ticket = 70% of base price, VIP = 150%
- Customer name and email are required; invalid email is rejected

## #4 Prevent double booking
As a cinema manager, I want the system to refuse seats that are already taken or don't exist so that two customers never get the same seat.
- Booking a taken seat throws SeatAlreadyBookedException
- A seat code that doesn't exist (e.g. Z99) throws InvalidSeatException
- If one seat in a multi-seat booking fails, no seats are booked
- Wrong menu input does not crash the program

## #5 Switchable pricing rules (Strategy pattern)
As a cinema manager, I want to switch between pricing rules while the program runs so that I can use weekend prices or discount days without changing the code.
- Rules: Standard, Weekend (+20% Sat/Sun), Cheap Tuesday (-50%)
- The active rule is shown in the menu header

## #6 Cancel bookings and save data between runs
As a cinema manager, I want to cancel bookings and keep all data after the program closes so that nothing is lost.
- Cancelling a booking frees its seats
- Data is saved to cinema.dat on exit and loaded on the next start
- On first start, sample movies and screenings are created
