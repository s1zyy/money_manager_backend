package vlad.corp.money_manager_backend.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import vlad.corp.money_manager_backend.application.auth.port.TokenGenerator;
import vlad.corp.money_manager_backend.application.invite.ClaimInviteWithLoginUseCase;
import vlad.corp.money_manager_backend.application.invite.InviteVirtualParticipantUseCase;
import vlad.corp.money_manager_backend.application.invite.ValidateInviteTokenUseCase;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import vlad.corp.money_manager_backend.domain.repository.VirtualParticipantInviteRepository;

@Configuration
public class InviteUseCaseConfig {

    @Bean
    public InviteVirtualParticipantUseCase inviteVirtualParticipantUseCase(
            TripRepository tripRepository,
            ParticipantRepository participantRepository,
            VirtualParticipantInviteRepository inviteRepository,
            JavaMailSender mailSender,
            @Value("${spring.mail.from}") String fromEmail) {
        return new InviteVirtualParticipantUseCase(
                tripRepository, participantRepository, inviteRepository, mailSender, fromEmail);
    }

    @Bean
    public ValidateInviteTokenUseCase validateInviteTokenUseCase(
            VirtualParticipantInviteRepository inviteRepository,
            TripRepository tripRepository,
            ParticipantRepository participantRepository) {
        return new ValidateInviteTokenUseCase(inviteRepository, tripRepository, participantRepository);
    }

    @Bean
    public ClaimInviteWithLoginUseCase claimInviteWithLoginUseCase(
            VirtualParticipantInviteRepository inviteRepository,
            ParticipantRepository participantRepository,
            TripRepository tripRepository,
            ExpenseRepository expenseRepository,
            PasswordEncoder passwordEncoder,
            TokenGenerator tokenGenerator) {
        return new ClaimInviteWithLoginUseCase(
                inviteRepository, participantRepository, tripRepository,
                expenseRepository, passwordEncoder, tokenGenerator);
    }
}
