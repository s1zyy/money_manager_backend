package vlad.corp.money_manager_backend.application.participant;

import org.springframework.security.crypto.password.PasswordEncoder;
import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.exceptions.BusinessException;
import vlad.corp.money_manager_backend.domain.model.Participant;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;

import java.util.UUID;

public class ChangePasswordUseCase {

    private final ParticipantRepository participantRepository;
    private final PasswordEncoder passwordEncoder;

    public ChangePasswordUseCase(ParticipantRepository participantRepository, PasswordEncoder passwordEncoder) {
        this.participantRepository = participantRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void execute(UUID participantId, String currentPassword, String newPassword) {
        Participant existing = participantRepository.findById(participantId)
                .orElseThrow(() -> new NotFoundException("Participant not found"));

        if (!passwordEncoder.matches(currentPassword, existing.getPasswordHash())) {
            throw new BusinessException("Current password is incorrect");
        }

        Participant updated = new Participant(
                existing.getId(),
                existing.getName(),
                existing.getEmail(),
                passwordEncoder.encode(newPassword),
                existing.isVirtual()
        );
        participantRepository.save(updated);
    }
}
