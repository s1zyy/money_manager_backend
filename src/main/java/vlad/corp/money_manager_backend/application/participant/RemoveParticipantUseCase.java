package vlad.corp.money_manager_backend.application.participant;

import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;

import java.util.UUID;

public class RemoveParticipantUseCase {
    private final TripRepository tripRepository;

    public RemoveParticipantUseCase(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public void execute(UUID tripId, UUID participantId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip not found with id: " + tripId));

        trip.ensureNotArchived();

        if(!trip.getParticipantIds().contains(participantId)) {
            throw new NotFoundException("Participant not found with id: " + participantId);
        }

        trip.getParticipantIds().remove(participantId);
        tripRepository.save(trip);
    }
}
