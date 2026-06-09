package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.JoinCode;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import java.util.UUID;

public class JoinTripUseCase {
    private final TripRepository tripRepository;

    public JoinTripUseCase(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip execute(
            String code,
            UUID participantId
    ) {
        JoinCode joinCode = new JoinCode(code);
        Trip trip = tripRepository.findByJoinCode(joinCode)
                .orElseThrow(() -> new NotFoundException("Trip not found with join code: " + code));
        trip.addParticipant(participantId);
        tripRepository.save(trip);
        return trip;
    }
}
