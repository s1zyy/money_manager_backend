package vlad.corp.money_manager_backend.application.participant;

import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Participant;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ListParticipantsUseCase {

    private final TripRepository tripRepository;
    private final ParticipantRepository participantRepository;

    public ListParticipantsUseCase(TripRepository tripRepository, ParticipantRepository participantRepository) {
        this.tripRepository = tripRepository;
        this.participantRepository = participantRepository;
    }

    public List<Participant> execute(UUID tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip not found with id: " + tripId));
        return trip.getParticipantIds().stream()
                .map(id -> participantRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Participant not found with id: " + id)))
                .collect(Collectors.toList());
    }
}
