package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import vlad.corp.money_manager_backend.domain.value_objects.Money;
import java.math.BigDecimal;
import java.util.UUID;

public class UpdateParticipantBudgetUseCase {

    private final TripRepository tripRepository;

    public UpdateParticipantBudgetUseCase(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip execute(UUID tripId, UUID participantId, BigDecimal budget) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip not found with id: " + tripId));
        trip.updateParticipantBudget(participantId, new Money(budget));
        tripRepository.save(trip);
        return trip;
    }
}
