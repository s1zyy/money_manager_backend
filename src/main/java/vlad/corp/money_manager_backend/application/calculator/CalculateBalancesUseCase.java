package vlad.corp.money_manager_backend.application.calculator;

import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.value_objects.Money;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CalculateBalancesUseCase {

    public Map<UUID, Money> execute(Trip trip, List<Expense> expenses) {

        Map<UUID, Money> balances = new HashMap<>();

        for (UUID participantId : trip.getParticipantIds()) {
            balances.put(participantId, Money.zero());
        }

        for (Expense expense : expenses) {
            if (expense.getPayerId() == null) {
                continue;
            }

            Money totalAmount = expense.getAmount();
            UUID payerId = expense.getPayerId();
            balances.put(payerId, balances.getOrDefault(payerId, Money.zero()).add(totalAmount));

            Map<UUID, BigDecimal> shares = expense.getParticipantShares();
            int count = shares.size();

            for (Map.Entry<UUID, BigDecimal> entry : shares.entrySet()) {
                UUID participantId = entry.getKey();
                BigDecimal shareAmount = entry.getValue() != null
                        ? entry.getValue()
                        : totalAmount.amount().divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);

                Money share = new Money(shareAmount);
                Money current = balances.getOrDefault(participantId, Money.zero());
                balances.put(participantId, current.subtract(share));
            }
        }
        return balances;
    }
}
