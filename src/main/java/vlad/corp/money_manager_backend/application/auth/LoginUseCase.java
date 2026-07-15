package vlad.corp.money_manager_backend.application.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import vlad.corp.money_manager_backend.application.auth.port.TokenGenerator;
import vlad.corp.money_manager_backend.application.exception.InvalidCredentialsException;
import vlad.corp.money_manager_backend.domain.model.Participant;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;
import java.util.List;


public class LoginUseCase {
    private final ParticipantRepository participantRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;

    public LoginUseCase(ParticipantRepository participantRepository, PasswordEncoder passwordEncoder, TokenGenerator tokenGenerator) {
        this.participantRepository = participantRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenGenerator = tokenGenerator;
    }

    public String login(String email, String password) {
        Participant participant = participantRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
        if(!passwordEncoder.matches(password, participant.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        return tokenGenerator.generateToken(participant, List.of("ROLE_USER"));
    }
}
