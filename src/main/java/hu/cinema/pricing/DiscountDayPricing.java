package hu.cinema.pricing;

import hu.cinema.model.Screening;
import hu.cinema.model.Ticket;

import java.time.DayOfWeek;

/** "Cheap Tuesday": every ticket is half price on Tuesdays. */
public class DiscountDayPricing implements PricingStrategy {
    @Override
    public double calculatePrice(Ticket ticket, Screening screening) {
        double price = ticket.getPrice(screening.getBasePrice());
        if (screening.getStartTime().getDayOfWeek() == DayOfWeek.TUESDAY) {
            price = price * 0.5;
        }
        return price;
    }

    @Override
    public String getName() { return "Cheap Tuesday (-50%)"; }
}
