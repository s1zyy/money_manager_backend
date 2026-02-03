package vlad.corp.money_manager_backend.domain.model;

import lombok.Getter;
import vlad.corp.money_manager_backend.application.exception.ArchivedTripException;
import vlad.corp.money_manager_backend.application.exception.ParticipantAlreadyExistException;
import vlad.corp.money_manager_backend.domain.value_objects.Money;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
public class Trip {

    private final UUID id;
    private final UUID ownerId;
    private final String name;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Money totalBudget;
    private final Money prepaidExpenses;
    private final List<UUID> participantIds;
    private final JoinCode joinCode;
    private TripStatus status;

    public Trip(UUID id, UUID ownerId, String name, LocalDate startDate, LocalDate endDate, Money totalBudget, Money prepaidExpenses, List<UUID> participantIds, JoinCode joinCode) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalBudget = totalBudget;
        this.prepaidExpenses = prepaidExpenses;
        this.participantIds = participantIds;
        this.joinCode = joinCode;
    }

    public void archive() {
        this.status = TripStatus.ARCHIVED;
    }

    public void ensureNotArchived() {
        if (this.status == TripStatus.ARCHIVED) {
            throw new ArchivedTripException("Trip is archived and cannot be modified.");
        }
    }

    public void hasParticipantWithId(UUID id) {
        if (participantIds.contains(id)) {
            throw new ParticipantAlreadyExistException("Participant with id " + id + " already in trip.");
        }
    }


}
