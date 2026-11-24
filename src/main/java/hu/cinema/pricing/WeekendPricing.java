package hu.cinema.pricing;

import hu.cinema.model.Screening;
import hu.cinema.model.Ticket;

import java.time.DayOfWeek;

/** Screenings on Saturday or Sunday cost 20% more. */
public class WeekendPricing implements PricingStrategy {
    @Override
    public double calculatePrice(Ticket ticket, Screening screening) {
        double price = ticket.getPrice(screening.getBasePrice());
        DayOfWeek day = screening.getStartTime().getDayOfWeek();
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            price = price * 1.2;
        }
        return price;
    }

    @Override
    public String getName() { return "Weekend (+20% Sat/Sun)"; }
}
