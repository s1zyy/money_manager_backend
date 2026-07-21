package vlad.corp.money_manager_backend.application.participant;

import vlad.corp.money_manager_backend.domain.exceptions.BusinessException;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.model.TripStatus;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;

import java.util.List;
import java.util.UUID;

public class FullDeleteAccountUseCase {

    private final TripRepository tripRepository;
    private final ExpenseRepository expenseRepository;
    private final ParticipantRepository participantRepository;

    public FullDeleteAccountUseCase(TripRepository tripRepository,
                                    ExpenseRepository expenseRepository,
                                    ParticipantRepository participantRepository) {
        this.tripRepository = tripRepository;
        this.expenseRepository = expenseRepository;
        this.participantRepository = participantRepository;
    }

    public void execute(UUID participantId) {
        List<Trip> allTrips = tripRepository.findAllTripsForUser(participantId);

        boolean ownsActiveTrip = allTrips.stream()
                .anyMatch(t -> t.getOwnerId().equals(participantId) && t.getStatus() != TripStatus.ARCHIVED);
        if (ownsActiveTrip) {
            throw new BusinessException("You own active trips. Archive or transfer them before deleting your account.");
        }

        if (expenseRepository.hasAnyExpenseInvolvement(participantId)) {
            throw new BusinessException("You have transaction history. Use Deactivate Account instead.");
        }

        for (Trip trip : allTrips) {
            if (trip.getOwnerId().equals(participantId)) {
                tripRepository.deleteById(trip.getId());
            } else if (trip.getStatus() != TripStatus.ARCHIVED) {
                trip.leave(participantId);
                tripRepository.save(trip);
            }
        }

        participantRepository.deleteById(participantId);
    }
}
