package vlad.corp.money_manager_backend.domain.model;

import lombok.Getter;
import vlad.corp.money_manager_backend.application.exception.*;
import vlad.corp.money_manager_backend.domain.exceptions.ArchivedTripException;
import vlad.corp.money_manager_backend.domain.exceptions.OnlyOwnerCanArchiveTripException;
import vlad.corp.money_manager_backend.domain.exceptions.OwnerCannotLeaveTripException;
import vlad.corp.money_manager_backend.domain.value_objects.Money;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Trip {

    private final UUID id;
    private final UUID ownerId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Money totalBudget;
    private Money prepaidExpenses;
    private List<UUID> participantIds;
    private final JoinCode joinCode;
    private TripStatus status;

    public Trip(UUID id, UUID ownerId, String name, LocalDate startDate, LocalDate endDate, Money totalBudget, Money prepaidExpenses, List<UUID> participantIds, JoinCode joinCode, TripStatus status) {
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
        if(ownerId.equals(participantId)) {
            throw new OwnerCannotLeaveTripException("Owner cannot leave the trip. Consider archiving the trip instead.");
        }
        participantIds.remove(participantId);
    }

    public void updateName(String newName) {
        ensureNotArchived();
        if (newName != null && !newName.isBlank()) {
            this.name = newName;
        }
    }

    public void updateDates(LocalDate start, LocalDate end) {
        ensureNotArchived();
        if (start != null) this.startDate = start;
        if (end != null) this.endDate = end;
    }

    public void updateBudget(Money total, Money prepaid) {
        ensureNotArchived();
        if (total != null) this.totalBudget = total;
        if (prepaid != null) this.prepaidExpenses = prepaid;
    }

    public void updateParticipants(List<UUID> newParticipants) {
        ensureNotArchived();
        if (newParticipants != null) {
            if (!newParticipants.contains(this.ownerId)) {
                throw new NotFoundException("Owner must remain in participants");
            }
            this.participantIds.clear();
            this.participantIds.addAll(newParticipants);
        }
    }


}
