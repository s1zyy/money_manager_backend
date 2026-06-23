package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Participant;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import java.util.Set;
import java.util.UUID;

public class ListParticipantsUseCase {

    private final TripRepository tripRepository;
    private final ParticipantRepository participantRepository;

    public ListParticipantsUseCase(TripRepository tripRepository, ParticipantRepository participantRepository) {
        this.tripRepository = tripRepository;
        this.participantRepository = participantRepository;
    }

    public Set<Participant> execute(UUID tripId, UUID participantId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip not found with id: " + tripId));
        trip.ensureParticipant(participantId);
        return participantRepository.findAllByIds(trip.getParticipantIds());
    }
}
