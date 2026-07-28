package vlad.corp.money_manager_backend.domain.model;

import lombok.Getter;
import lombok.Setter;
import vlad.corp.money_manager_backend.application.exceptions.*;
import vlad.corp.money_manager_backend.domain.exceptions.*;
import vlad.corp.money_manager_backend.domain.value_objects.Money;
import java.time.LocalDate;
import java.util.Map;
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
    private String currency;
    private Map<UUID, Money> participantBudgets;
    private final JoinCode joinCode;
    private TripStatus status;

    public Trip(UUID id, UUID ownerId, String name, LocalDate startDate, LocalDate endDate,
                Map<UUID, Money> participantBudgets, JoinCode joinCode, TripStatus status, String currency) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.participantBudgets = participantBudgets;
        this.joinCode = joinCode;
        this.status = Objects.requireNonNull(status, "Trip status must not be null");
        this.currency = currency;
    }

    public Set<UUID> getParticipantIds() {
        return participantBudgets.keySet();
    }

    public Money getMyBudget(UUID participantId) {
        return participantBudgets.getOrDefault(participantId, Money.zero());
    }

    public void archive(UUID participantId) {
        if (!ownerId.equals(participantId)) {
            throw new OnlyOwnerCanArchiveTripException(participantId);
        }
        this.status = TripStatus.ARCHIVED;
    }

    public void unarchive(UUID participantId) {
        if (!ownerId.equals(participantId)) {
            throw new OnlyOwnerCanArchiveTripException(participantId);
        }
        LocalDate today = LocalDate.now();
        this.status = today.isBefore(startDate) ? TripStatus.UPCOMING : TripStatus.ACTIVE;
    }

    public void ensureNotArchived() {
        if (this.status == TripStatus.ARCHIVED) {
            throw new ArchivedTripException("This trip is archived and can't be modified.");
        }
    }

    public void ensureParticipant(UUID participantId) {
        if (!participantBudgets.containsKey(participantId)) {
            throw new NotFoundException("Participant with id " + participantId + " not found in trip with id " + id);
        }
    }

    public void leave(UUID participantId) {
        ensureNotArchived();
        ensureParticipant(participantId);
        ensureNotOwner(participantId);
        participantBudgets.remove(participantId);
    }

    public void removeParticipant(UUID ownerId, UUID toRemove) {
        ensureNotArchived();
        ensureOwner(ownerId);
        ensureParticipant(toRemove);
        ensureNotOwner(toRemove);
        participantBudgets.remove(toRemove);
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
        if (ownerId.equals(participantId)) {
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

    public void updateParticipantBudget(UUID participantId, Money budget) {
        ensureNotArchived();
        ensureParticipant(participantId);
        participantBudgets.put(participantId, budget);
    }

    public void addParticipant(UUID participantId, Money budget) {
        ensureNotArchived();
        if (this.participantBudgets.containsKey(participantId)) {
            throw new ParticipantAlreadyExistException("You are already a participant in this trip");
        }
        if (this.participantBudgets.size() >= 10) {
            throw new BusinessException("Cannot add more than 10 participants to a trip");
        }
        this.participantBudgets.put(participantId, budget);
    }
}
