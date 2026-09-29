package vlad.corp.money_manager_backend.application.calculator;

import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.model.SplitMode;
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

            if (expense.getSplitMode() == SplitMode.EQUAL) {
                applyEqualSplit(balances, shares.keySet().stream().toList(), totalAmount);
            } else {
                for (Map.Entry<UUID, BigDecimal> entry : shares.entrySet()) {
                    Money share = new Money(entry.getValue());
                    balances.put(entry.getKey(), balances.getOrDefault(entry.getKey(), Money.zero()).subtract(share));
                }
            }
        }
        return balances;
    }

    // Distributes totalAmount across participants without losing cents.
    // Base share goes to everyone; the remainder (in cents) is given
    // one cent at a time to the first participants in iteration order.
    private void applyEqualSplit(Map<UUID, Money> balances, List<UUID> participants, Money totalAmount) {
        long totalCents = totalAmount.amount()
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .longValue();
        int count = participants.size();
        long baseCents = totalCents / count;
        long remainder = totalCents % count;

        for (int i = 0; i < count; i++) {
            long cents = baseCents + (i < remainder ? 1 : 0);
            Money share = new Money(BigDecimal.valueOf(cents, 2));
            UUID id = participants.get(i);
            balances.put(id, balances.getOrDefault(id, Money.zero()).subtract(share));
        }
    }
}
