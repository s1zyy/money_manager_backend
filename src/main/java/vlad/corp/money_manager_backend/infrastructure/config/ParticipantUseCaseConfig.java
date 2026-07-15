package vlad.corp.money_manager_backend.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vlad.corp.money_manager_backend.application.participant.ChangePasswordUseCase;
import vlad.corp.money_manager_backend.application.participant.UpdateProfileUseCase;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;

@Configuration
public class ParticipantUseCaseConfig {

    @Bean
    public UpdateProfileUseCase updateProfileUseCase(ParticipantRepository participantRepository) {
        return new UpdateProfileUseCase(participantRepository);
    }

    @Bean
    public ChangePasswordUseCase changePasswordUseCase(ParticipantRepository participantRepository,
                                                       PasswordEncoder passwordEncoder) {
        return new ChangePasswordUseCase(participantRepository, passwordEncoder);
    }
}
