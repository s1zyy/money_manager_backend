package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.domain.model.Participant;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;

import vlad.corp.money_manager_backend.domain.exceptions.BusinessException;

import java.util.Set;
import java.util.UUID;

public class AddVirtualParticipantUseCase {

    private final TripRepository tripRepository;
    private final ParticipantRepository participantRepository;

    public AddVirtualParticipantUseCase(TripRepository tripRepository, ParticipantRepository participantRepository) {
        this.tripRepository = tripRepository;
        this.participantRepository = participantRepository;
    }

    public Participant execute(UUID tripId, UUID ownerId, String name) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new vlad.corp.money_manager_backend.application.exception.NotFoundException("Trip not found"));

        trip.ensureNotArchived();
        trip.ensureOwner(ownerId);

        Set<Participant> existing = participantRepository.findAllByIds(trip.getParticipantIds());
        boolean nameExists = existing.stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(name.trim()));
        if (nameExists) {
            throw new BusinessException("Participant with name '" + name + "' already exists in this trip");
        }

        Participant virtual = new Participant(UUID.randomUUID(), name.trim(), null, null, true);
        participantRepository.save(virtual);

        trip.addParticipant(virtual.getId());
        tripRepository.save(trip);

        return virtual;
    }
}