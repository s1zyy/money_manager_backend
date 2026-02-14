package vlad.corp.money_manager_backend.application.calculator;

import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.value_objects.Money;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CalculateBalancesUseCase {

    public Map<UUID, Money> execute(Trip trip, List<Expense> expenses) {

        Map<UUID, Money> balances = new HashMap<>();

        for(UUID participantId: trip.getParticipantIds()) {
            balances.put(participantId, Money.zero());
        }

        for(Expense expense: expenses) {
            Money totalAmount = expense.getAmount();
            UUID payerId = expense.getPayerId();
            balances.put(payerId, balances.get(payerId).add(totalAmount));

            int participantsCount = expense.getParticipantIds().size();
            Money share = totalAmount.divide(
                    BigDecimal.valueOf(participantsCount)
            );
            for(UUID participantId: expense.getParticipantIds()) {
                balances.put(
                        participantId,
                        balances.get(participantId)
                                .subtract(share)
                );
            }
        }
        return balances;
    }
}
