package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.application.exceptions.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;

import java.util.UUID;

public class GetTripUseCase {
    private final TripRepository tripRepository;

    public GetTripUseCase(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip execute(UUID tripId, UUID userId) {
        Trip trip =  tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip with id " + tripId + " not found"));
        trip.ensureParticipant(userId);
        return trip;
    }
}
