package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.application.exceptions.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;

import java.util.UUID;

public class LeaveTripUseCase {
    private final TripRepository tripRepository;

    public LeaveTripUseCase(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public void execute(UUID tripId, UUID participantId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip with id " + tripId + " not found"));
        trip.leave(participantId);
        tripRepository.save(trip);
    }
}
