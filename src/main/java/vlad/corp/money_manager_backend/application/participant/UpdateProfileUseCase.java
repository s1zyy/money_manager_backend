package vlad.corp.money_manager_backend.application.participant;

import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Participant;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;

import java.util.UUID;

public class UpdateProfileUseCase {

    private final ParticipantRepository participantRepository;

    public UpdateProfileUseCase(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public Participant execute(UUID participantId, String newName) {
        Participant existing = participantRepository.findById(participantId)
                .orElseThrow(() -> new NotFoundException("Participant not found"));

        Participant updated = new Participant(
                existing.getId(),
                newName,
                existing.getEmail(),
                existing.getPasswordHash(),
                existing.isVirtual()
        );
        participantRepository.save(updated);
        return updated;
    }
}
