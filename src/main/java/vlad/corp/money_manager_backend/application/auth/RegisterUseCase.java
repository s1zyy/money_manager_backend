package vlad.corp.money_manager_backend.application.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import vlad.corp.money_manager_backend.application.auth.port.TokenGenerator;
import vlad.corp.money_manager_backend.application.exception.UserAlreadyExistsException;
import vlad.corp.money_manager_backend.domain.model.User;
import vlad.corp.money_manager_backend.domain.repository.UserRepository;
import java.util.List;
import java.util.UUID;

public class RegisterUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;

    public RegisterUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenGenerator tokenGenerator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenGenerator = tokenGenerator;
    }

    public String register(String email, String password) {
        if(userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException(email);
        }
        User user = new User(UUID.randomUUID(), email, passwordEncoder.encode(password));
        userRepository.save(user);
        return tokenGenerator.generateToken(user, List.of("ROLE_USER"));


    }
}
