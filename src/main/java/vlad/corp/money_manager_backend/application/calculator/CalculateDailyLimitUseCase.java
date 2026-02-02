package vlad.corp.money_manager_backend.application.calculator;

import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.value_objects.Money;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class CalculateDailyLimitUseCase {

    public Money execute(Trip trip, List<Expense> expenses) {
        Money totalSpent = expenses.stream()
                .map(Expense::getAmount)
                .reduce(Money.zero(), Money::add);

        Money operationalBudget =
                trip.getTotalBudget().subtract(trip.getPrepaidExpenses());
        Money remainingBudget =
                operationalBudget.subtract(totalSpent);

        long remainingDays = daysRemaining(trip);

        if(remainingDays <= 0) {
            return Money.zero();
        }

        return remainingBudget.divide(
                BigDecimal.valueOf(remainingDays)
        );
    }

    private long daysRemaining(Trip trip) {
        LocalDate today = LocalDate.now();
        if(today.isAfter(trip.getEndDate())) {
            return 0;
        }

        LocalDate start =
                today.isBefore(trip.getStartDate())
                        ? trip.getStartDate()
                        : today;

        return start.datesUntil(trip.getEndDate().plusDays(1)).count();


    }
}
