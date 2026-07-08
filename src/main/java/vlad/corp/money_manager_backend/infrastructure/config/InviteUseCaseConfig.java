package vlad.corp.money_manager_backend.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import vlad.corp.money_manager_backend.application.invite.InviteVirtualParticipantUseCase;
import vlad.corp.money_manager_backend.application.invite.ValidateInviteTokenUseCase;
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
            @Value("${spring.mail.username}") String fromEmail) {
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
}
