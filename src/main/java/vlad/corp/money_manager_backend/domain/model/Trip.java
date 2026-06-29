package vlad.corp.money_manager_backend.domain.model;

import lombok.Getter;
import lombok.Setter;
import vlad.corp.money_manager_backend.application.exception.*;
import vlad.corp.money_manager_backend.domain.exceptions.*;
import vlad.corp.money_manager_backend.domain.value_objects.Money;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class Trip {

    private final UUID id;
    private final UUID ownerId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Money totalBudget;
    private Money prepaidExpenses;
    private String currency;
    private Set<UUID> participantIds;
    private final JoinCode joinCode;
    private TripStatus status;

    public Trip(UUID id, UUID ownerId, String name, LocalDate startDate, LocalDate endDate, Money totalBudget, Money prepaidExpenses, Set<UUID> participantIds, JoinCode joinCode, TripStatus status, String currency) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalBudget = totalBudget;
        this.prepaidExpenses = prepaidExpenses;
        this.participantIds = participantIds;
        this.joinCode = joinCode;
        this.status = Objects.requireNonNull(status, "Trip status must not be null");
        this.currency = currency;
    }

    public void archive(UUID participantId) {
        if(!ownerId.equals(participantId)) {
            throw new OnlyOwnerCanArchiveTripException(participantId);
        }
        this.status = TripStatus.ARCHIVED;
    }

    public void ensureNotArchived() {
        if (this.status == TripStatus.ARCHIVED) {
            throw new ArchivedTripException("Trip with id " + this.id + " is archived and can't be modified.");
        }
    }

    public void ensureParticipant(UUID participantId) {
        if(!participantIds.contains(participantId)) {
            throw new NotFoundException("Participant with id " + participantId + " not found in trip with id " + id);
        }
    }

    public void leave(UUID participantId) {
        ensureNotArchived();
        ensureParticipant(participantId);
        ensureNotOwner(participantId);
        participantIds.remove(participantId);
    }

    public void removeParticipant(UUID ownerId, UUID toRemove) {
        ensureNotArchived();
        ensureOwner(ownerId);
        ensureParticipant(toRemove);
        ensureNotOwner(toRemove);

        participantIds.remove(toRemove);
    }

    public void updateName(String newName) {
        ensureNotArchived();
        if (newName != null && !newName.isBlank()) {
            this.name = newName;
        }
    }
    public void ensureOwner(UUID participantId) {
        ensureNotArchived();
        if (!ownerId.equals(participantId)) {
            throw new ForbiddenException("Only the owner can perform this action");
        }
    }
    public void ensureNotOwner(UUID participantId) {
        if(ownerId.equals(participantId)) {
            throw new OwnerCannotLeaveTripException("Owner cannot perform this action");
        }
    }

    public void updateStartDate(LocalDate start) {
        ensureNotArchived();
        if (start != null) this.startDate = start;
    }

    public void updateEndDate(LocalDate end) {
        ensureNotArchived();
        if (end != null) this.endDate = end;
    }

    public void updateBudget(Money total, Money prepaid) {
        ensureNotArchived();
        if (total != null) this.totalBudget = total;
        if (prepaid != null) this.prepaidExpenses = prepaid;
    }


    public void addParticipant(UUID participantId) {
        ensureNotArchived();
        if (this.participantIds.contains(participantId)) {
            throw new ParticipantAlreadyExistException("Participant with id " + participantId + " already exists in trip with id " + id);
        }
        if(this.participantIds.size()>=10) {
            throw new BusinessException("Cannot add more than 10 participants to a trip"); }
        this.participantIds.add(participantId);
    }

}
