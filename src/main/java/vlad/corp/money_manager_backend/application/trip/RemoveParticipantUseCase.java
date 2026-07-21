package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.application.exceptions.NotFoundException;
import vlad.corp.money_manager_backend.domain.exceptions.BusinessException;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;

import java.util.Set;
import java.util.UUID;

public class RemoveParticipantUseCase {
    private final TripRepository tripRepository;
    private final ExpenseRepository expenseRepository;
    private final ParticipantRepository participantRepository;

    public RemoveParticipantUseCase(TripRepository tripRepository, ExpenseRepository expenseRepository, ParticipantRepository participantRepository) {
        this.tripRepository = tripRepository;
        this.expenseRepository = expenseRepository;
        this.participantRepository = participantRepository;
    }

    public void execute(UUID tripId, UUID requester, UUID toRemove) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip not found"));
        Set<UUID> activeParticipants = expenseRepository.findActiveParticipantIds(tripId);
        if(activeParticipants.contains(toRemove)) {
            throw new BusinessException("This user has expenses, you can not delete him");
        }
        trip.removeParticipant(requester, toRemove);
        tripRepository.save(trip);

        participantRepository.findById(toRemove).ifPresent(p -> {
            if(p.isVirtual()) {
                participantRepository.deleteById(toRemove);
            }
        });



    }
}
