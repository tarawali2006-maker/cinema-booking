package hu.cinema.pricing;

import hu.cinema.model.Screening;
import hu.cinema.model.Ticket;

/** No extra change: just the ticket type's price. */
public class StandardPricing implements PricingStrategy {
    @Override
    public double calculatePrice(Ticket ticket, Screening screening) {
        return ticket.getPrice(screening.getBasePrice());
    }

    @Override
    public String getName() { return "Standard"; }
}
