package vlad.corp.money_manager_backend.application.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import vlad.corp.money_manager_backend.application.auth.port.TokenGenerator;
import vlad.corp.money_manager_backend.application.exceptions.NotFoundException;
import vlad.corp.money_manager_backend.domain.exceptions.BusinessException;
import vlad.corp.money_manager_backend.domain.exceptions.ParticipantAlreadyExistException;
import vlad.corp.money_manager_backend.domain.model.Participant;
import vlad.corp.money_manager_backend.domain.model.VirtualParticipantInvite;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;
import vlad.corp.money_manager_backend.domain.repository.VirtualParticipantInviteRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class RegisterUseCase {

    private final ParticipantRepository participantRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;
    private final VirtualParticipantInviteRepository inviteRepository;

    public RegisterUseCase(ParticipantRepository participantRepository,
                           PasswordEncoder passwordEncoder,
                           TokenGenerator tokenGenerator,
                           VirtualParticipantInviteRepository inviteRepository) {
        this.participantRepository = participantRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenGenerator = tokenGenerator;
        this.inviteRepository = inviteRepository;
    }

    public String register(String email, String password, String name, String inviteToken) {
        if (participantRepository.findByEmail(email).isPresent()) {
            throw new ParticipantAlreadyExistException("Participant with email " + email + " already exists");
        }

        if (inviteToken != null && !inviteToken.isBlank()) {
            return claimVirtualParticipant(email, password, inviteToken);
        }

        Participant participant = new Participant(UUID.randomUUID(), name, email, passwordEncoder.encode(password), false);
        participantRepository.save(participant);
        return tokenGenerator.generateToken(participant, List.of("ROLE_USER"));
    }

    private String claimVirtualParticipant(String email, String password, String token) {
        VirtualParticipantInvite invite = inviteRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("Invite not found"));

        if (invite.expiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Invite has expired");
        }

        Participant virtual = participantRepository.findById(invite.virtualParticipantId())
                .orElseThrow(() -> new NotFoundException("Virtual participant not found"));

        Participant claimed = new Participant(
                virtual.getId(),
                virtual.getName(),
                email,
                passwordEncoder.encode(password),
                false
        );
        participantRepository.save(claimed);
        inviteRepository.deleteByToken(token);

        return tokenGenerator.generateToken(claimed, List.of("ROLE_USER"));
    }
}
