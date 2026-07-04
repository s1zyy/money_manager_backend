package vlad.corp.money_manager_backend.domain.model;

import lombok.Getter;
import vlad.corp.money_manager_backend.domain.exceptions.InvalidExpenseDateException;
import vlad.corp.money_manager_backend.domain.value_objects.Money;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Getter
public class Expense {

    private final UUID id;
    private final UUID tripId;
    private final Money amount;
    private final UUID payerId; // null = each participant paid their own share
    private final SplitMode splitMode;
    private final Map<UUID, BigDecimal> participantShares; // value null = legacy EQUAL row
    private final LocalDate date;
    private final String description;
    private final boolean isPrepaid;

    public Expense(UUID id, UUID tripId, Money amount, UUID payerId, SplitMode splitMode,
                   Map<UUID, BigDecimal> participantShares, LocalDate date, String description,
                   boolean isPrepaid) {
        if (!isPrepaid && date != null && date.isAfter(LocalDate.now())) {
            throw new InvalidExpenseDateException("Expense date is after now");
        }
        this.id = id;
        this.tripId = tripId;
        this.amount = amount;
        this.payerId = payerId;
        this.splitMode = splitMode;
        this.participantShares = participantShares;
        this.date = date;
        this.description = description;
        this.isPrepaid = isPrepaid;
    }

    public Set<UUID> getParticipantIds() {
        return participantShares.keySet();
    }
}
