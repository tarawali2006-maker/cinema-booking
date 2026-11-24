package hu.cinema.pricing;

import hu.cinema.model.Screening;
import hu.cinema.model.Ticket;

/**
 * STRATEGY pattern: the rule for calculating the final price of a ticket.
 * BookingService uses this interface and does not know which rule is active.
 */
public interface PricingStrategy {
    double calculatePrice(Ticket ticket, Screening screening);

    String getName();
}
