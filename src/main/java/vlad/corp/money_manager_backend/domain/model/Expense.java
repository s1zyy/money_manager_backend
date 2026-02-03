package vlad.corp.money_manager_backend.domain.model;

import lombok.Getter;
import vlad.corp.money_manager_backend.domain.value_objects.Money;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
public class Expense {

    private final UUID id;
    private final UUID tripId;
    private final Money amount;
    private final UUID payerId;
    private final List<UUID> participantIds;
    private final LocalDate date;
    private final String description;

    public Expense(UUID id, UUID tripId, Money amount, UUID payerId, List<UUID> participantIds, LocalDate date, String description) {
        this.id = id;
        this.tripId = tripId;
        this.amount = amount;
        this.payerId = payerId;
        this.participantIds = participantIds;
        this.date = date;
        this.description = description;
    }
}
