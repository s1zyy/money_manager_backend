package vlad.corp.money_manager_backend.application.participant;

import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import java.util.UUID;

public class AddParticipantUseCase {

    private final TripRepository tripRepository;

    public AddParticipantUseCase(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public void addParticipant(UUID tripId, UUID participantId) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(()-> new NotFoundException("Trip not found with id: " + tripId));

        trip.ensureNotArchived();

        trip.hasParticipantWithId(participantId);

        if (trip.getParticipantIds().size() >= 10) {
            throw new IllegalStateException("Cannot add more than 10 participants to a trip");
        }

        trip.getParticipantIds().add(participantId);
        tripRepository.save(trip);

    }
}
