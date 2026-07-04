package vlad.corp.money_manager_backend.application.calculator;

import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.value_objects.Money;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CalculateDailyLimitUseCase {

    public Money execute(UUID participantId, Trip trip, List<Expense> expenses) {
        LocalDate today = LocalDate.now();

        if (today.isAfter(trip.getEndDate())) {
            return Money.zero();
        }

        Money myBudget = trip.getMyBudget(participantId);

        Money myPrepaidShare = expenses.stream()
                .filter(Expense::isPrepaid)
                .map(e -> getMyShare(participantId, e))
                .reduce(Money.zero(), Money::add);

        Money mySpentBeforeToday = expenses.stream()
                .filter(e -> !e.isPrepaid() && e.getDate() != null && e.getDate().isBefore(today))
                .map(e -> getMyShare(participantId, e))
                .reduce(Money.zero(), Money::add);

        long remainingDays = daysRemaining(trip);
        if (remainingDays <= 0) {
            return Money.zero();
        }

        return myBudget.subtract(myPrepaidShare).subtract(mySpentBeforeToday)
                .divide(BigDecimal.valueOf(remainingDays));
    }

    public static Money getMyShare(UUID participantId, Expense expense) {
        Map<UUID, BigDecimal> shares = expense.getParticipantShares();
        if (!shares.containsKey(participantId)) {
            return Money.zero();
        }
        BigDecimal shareAmount = shares.get(participantId);
        if (shareAmount == null) {
            return new Money(expense.getAmount().amount()
                    .divide(BigDecimal.valueOf(shares.size()), 2, RoundingMode.HALF_UP));
        }
        return new Money(shareAmount);
    }

    private long daysRemaining(Trip trip) {
        LocalDate today = LocalDate.now();
        if (today.isAfter(trip.getEndDate())) {
            return 0;
        }
        LocalDate start = today.isBefore(trip.getStartDate()) ? trip.getStartDate() : today;
        return start.datesUntil(trip.getEndDate().plusDays(1)).count();
    }
}
