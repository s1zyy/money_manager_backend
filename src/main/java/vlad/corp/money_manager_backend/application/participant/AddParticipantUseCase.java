package vlad.corp.money_manager_backend.application.participant;

import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.exceptions.ParticipantAlreadyExistException;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import java.util.UUID;

public class AddParticipantUseCase {

    private final TripRepository tripRepository;

    public AddParticipantUseCase(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public void execute(UUID tripId, UUID participantId) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(()-> new NotFoundException("Trip not found with id: " + tripId));

        trip.ensureNotArchived();

        if(trip.getParticipantIds().contains(participantId)) {
            throw new ParticipantAlreadyExistException("Participant with id " + participantId + " is already part of the trip");
        }

        if (trip.getParticipantIds().size() >= 10) {
            throw new IllegalStateException("Cannot add more than 10 participants to a trip");
        }

        trip.getParticipantIds().add(participantId);
        tripRepository.save(trip);

    }
}
