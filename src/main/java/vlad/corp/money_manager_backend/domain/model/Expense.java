package vlad.corp.money_manager_backend.domain.model;

import lombok.Getter;
import vlad.corp.money_manager_backend.domain.exceptions.InvalidExpenseDateException;
import vlad.corp.money_manager_backend.domain.value_objects.Money;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
public class Expense {

    private final UUID id;
    private final UUID tripId;
    private final Money amount;
    private final UUID payerId;
    private final Set<UUID> participantIds;
    private final LocalDate date;
    private final String description;

    public Expense(UUID id, UUID tripId, Money amount, UUID payerId, Set<UUID> participantIds, LocalDate date, String description) {
        if(date.isAfter(LocalDate.now())) {
            throw new InvalidExpenseDateException("Expense date is after now");
        }
        this.id = id;
        this.tripId = tripId;
        this.amount = amount;
        this.payerId = payerId;
        this.participantIds = participantIds;
        this.date = date;
        this.description = description;
    }
}
