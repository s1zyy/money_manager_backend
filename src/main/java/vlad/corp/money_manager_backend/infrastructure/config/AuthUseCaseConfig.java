package vlad.corp.money_manager_backend.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import vlad.corp.money_manager_backend.application.auth.LoginUseCase;
import vlad.corp.money_manager_backend.application.auth.RegisterUseCase;
import vlad.corp.money_manager_backend.application.auth.port.TokenGenerator;
import vlad.corp.money_manager_backend.domain.repository.UserRepository;

@Configuration
public class AuthUseCaseConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RegisterUseCase registerUseCase(UserRepository userRepository,
                                           PasswordEncoder encoder,
                                           TokenGenerator tokenGenerator) {
        return new RegisterUseCase(userRepository, encoder, tokenGenerator);
    }

    @Bean
    public LoginUseCase loginUseCase(UserRepository userRepository,
                                     PasswordEncoder encoder,
                                     TokenGenerator tokenGenerator) {
        return new LoginUseCase(userRepository, encoder, tokenGenerator);
    }
}
