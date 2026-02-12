package vlad.corp.money_manager_backend.domain.policy;

import vlad.corp.money_manager_backend.domain.exceptions.ArchivedTripException;
import vlad.corp.money_manager_backend.application.exception.ForbiddenException;
import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.model.TripStatus;

import java.util.UUID;

public class TripAccessPolicy {

    public void ensureParticipant(Trip trip, UUID participantId) {
        if (!trip.getParticipantIds().contains(participantId)) {
            throw new NotFoundException("User with id: " + participantId + " is not a participant of the trip with id: " + trip.getId());
        }
    }

    public void ensureOwner(Trip trip, UUID participantId) {
        if (!trip.getOwnerId().equals(participantId)) {
            throw new ForbiddenException("User with id: " + participantId + " is not the owner of the trip with id: " + trip.getId());
        }
    }

    public void ensureNotArchived(Trip trip) {
        if (trip.getStatus() == TripStatus.ARCHIVED) {
            throw new ArchivedTripException("Trip with id: " + trip.getId() + " is archived and can't be modified.");
        }
    }



    public void ensureCanUpdateExpense(Trip trip, Expense expense, UUID participantId) {
        ensureNotArchived(trip);
        ensureOwner(trip, participantId);
        ensureCorrectTrip(expense, trip);
        if (!expense.getPayerId().equals(participantId)) {
            throw new ForbiddenException("User with id: " + participantId + " is not the payer of the expense with id: " + expense.getId()); }
    }
    public void ensureCorrectTrip(Expense expense, Trip trip) {
        if (!expense.getTripId().equals(trip.getId())) {
            throw new ForbiddenException("Expense with id: " + expense.getId() + " does not belong to the trip with id: " + trip.getId());
        }
    }

    public void ensureCanDeleteExpense(Trip trip, Expense expense, UUID participantId) {
        ensureNotArchived(trip);
        ensureParticipant(trip, participantId);
        ensureCorrectTrip(expense, trip);

        boolean isPayer = expense.getPayerId().equals(participantId);
        boolean isOwner = trip.getOwnerId().equals(participantId);
        if(!isPayer && !isOwner) {
            throw new ForbiddenException("User with id: " + participantId + " is not the payer or the owner of the trip with id: " + trip.getId());
        }
    }
}
