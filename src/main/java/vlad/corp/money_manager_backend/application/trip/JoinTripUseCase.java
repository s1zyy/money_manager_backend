package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import java.util.UUID;

public class JoinTripUseCase {
    private final TripRepository tripRepository;

    public JoinTripUseCase(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public boolean execute(
            UUID tripId,
            UUID participantId
    ) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip not found: " + tripId));
        if(!trip.getParticipantIds().contains(participantId)) {
            trip.getParticipantIds().add(participantId);
            tripRepository.save(trip);
        }
        return true;
    }
}
