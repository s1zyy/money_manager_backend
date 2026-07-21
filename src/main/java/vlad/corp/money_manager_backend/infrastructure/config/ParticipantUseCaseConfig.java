package vlad.corp.money_manager_backend.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vlad.corp.money_manager_backend.application.calculator.CalculateBalancesUseCase;
import vlad.corp.money_manager_backend.application.participant.ChangePasswordUseCase;
import vlad.corp.money_manager_backend.application.participant.DeleteAccountUseCase;
import vlad.corp.money_manager_backend.application.participant.FullDeleteAccountUseCase;
import vlad.corp.money_manager_backend.application.participant.UpdateProfileUseCase;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;

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

    @Bean
    public DeleteAccountUseCase deleteAccountUseCase(TripRepository tripRepository,
                                                     ExpenseRepository expenseRepository,
                                                     CalculateBalancesUseCase calculateBalancesUseCase,
                                                     ParticipantRepository participantRepository) {
        return new DeleteAccountUseCase(tripRepository, expenseRepository, calculateBalancesUseCase, participantRepository);
    }

    @Bean
    public FullDeleteAccountUseCase fullDeleteAccountUseCase(TripRepository tripRepository,
                                                             ExpenseRepository expenseRepository,
                                                             ParticipantRepository participantRepository) {
        return new FullDeleteAccountUseCase(tripRepository, expenseRepository, participantRepository);
    }
}
