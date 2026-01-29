package vlad.corp.money_manager_backend.application.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import vlad.corp.money_manager_backend.application.auth.port.TokenGenerator;
import vlad.corp.money_manager_backend.domain.model.User;
import vlad.corp.money_manager_backend.domain.repository.UserRepository;


public class LoginUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;

    public LoginUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenGenerator tokenGenerator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenGenerator = tokenGenerator;
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        if(!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }
        return tokenGenerator.generateToken(user);
    }
}
