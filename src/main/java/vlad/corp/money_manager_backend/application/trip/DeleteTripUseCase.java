package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;

import java.util.UUID;

public class DeleteTripUseCase {
    private final TripRepository tripRepository;
    private final ExpenseRepository expenseRepository;

    public DeleteTripUseCase(TripRepository tripRepository, ExpenseRepository expenseRepository) {
        this.tripRepository = tripRepository;
        this.expenseRepository = expenseRepository;
    }

    public void execute(UUID tripId, UUID participantId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip not found"));
        trip.ensureOwner(participantId);
        expenseRepository.deleteAllByTripId(tripId);
        tripRepository.deleteById(tripId);
    }
}
