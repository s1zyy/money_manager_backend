package vlad.corp.money_manager_backend.application.calculator;

import vlad.corp.money_manager_backend.domain.value_objects.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class CalculateSettlementUseCase {

    public List<SettlementTransfer> execute(Map<UUID, Money> balances) {
        // creditors: balance > 0 (paid more than their share)
        // debtors: balance < 0 (paid less than their share)
        PriorityQueue<long[]> creditors = new PriorityQueue<>((a, b) -> Long.compare(b[1], a[1]));
        PriorityQueue<long[]> debtors = new PriorityQueue<>((a, b) -> Long.compare(b[1], a[1]));

        // Work in cents to avoid floating point issues
        Map<Long, UUID> indexToId = new HashMap<>();
        long counter = 0;

        for (Map.Entry<UUID, Money> entry : balances.entrySet()) {
            long cents = entry.getValue().amount().multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).longValue();
            if (cents > 0) {
                long idx = counter++;
                indexToId.put(idx, entry.getKey());
                creditors.offer(new long[]{idx, cents});
            } else if (cents < 0) {
                long idx = counter++;
                indexToId.put(idx, entry.getKey());
                debtors.offer(new long[]{idx, -cents});
            }
        }

        List<SettlementTransfer> transfers = new ArrayList<>();

        while (!creditors.isEmpty() && !debtors.isEmpty()) {
            long[] creditor = creditors.poll();
            long[] debtor = debtors.poll();

            long settle = Math.min(creditor[1], debtor[1]);

            UUID fromId = indexToId.get(debtor[0]);
            UUID toId = indexToId.get(creditor[0]);
            Money amount = new Money(BigDecimal.valueOf(settle, 2));

            transfers.add(new SettlementTransfer(fromId, toId, amount));

            long creditorRem = creditor[1] - settle;
            long debtorRem = debtor[1] - settle;

            if (creditorRem > 0) creditors.offer(new long[]{creditor[0], creditorRem});
            if (debtorRem > 0) debtors.offer(new long[]{debtor[0], debtorRem});
        }

        return transfers;
    }
}
