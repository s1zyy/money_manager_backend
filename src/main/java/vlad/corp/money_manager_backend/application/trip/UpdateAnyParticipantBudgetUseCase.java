package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import vlad.corp.money_manager_backend.domain.value_objects.Money;

import java.math.BigDecimal;
import java.util.UUID;

public class UpdateAnyParticipantBudgetUseCase {

    private final TripRepository tripRepository;

    public UpdateAnyParticipantBudgetUseCase(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public void execute(UUID tripId, UUID ownerId, UUID targetParticipantId, BigDecimal budget) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip not found"));
        trip.ensureOwner(ownerId);
        trip.updateParticipantBudget(targetParticipantId, new Money(budget));
        tripRepository.save(trip);
    }
}
