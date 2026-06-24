package vlad.corp.money_manager_backend.application.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import vlad.corp.money_manager_backend.application.auth.port.TokenGenerator;
import vlad.corp.money_manager_backend.domain.exceptions.ParticipantAlreadyExistException;
import vlad.corp.money_manager_backend.domain.model.Participant;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;
import java.util.List;
import java.util.UUID;

public class RegisterUseCase {

    private final ParticipantRepository participantRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;

    public RegisterUseCase(ParticipantRepository participantRepository, PasswordEncoder passwordEncoder, TokenGenerator tokenGenerator) {
        this.participantRepository = participantRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenGenerator = tokenGenerator;
    }

    public String register(String email, String password, String name) {
        if(participantRepository.findByEmail(email).isPresent()) {
            throw new ParticipantAlreadyExistException("Participant with email " + email + " already exists");
        }
        Participant participant = new Participant(UUID.randomUUID(), name, email, passwordEncoder.encode(password), false);
        participantRepository.save(participant);
        return tokenGenerator.generateToken(participant, List.of("ROLE_USER"));


    }
}
