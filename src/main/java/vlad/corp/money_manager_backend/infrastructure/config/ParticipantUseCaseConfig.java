package vlad.corp.money_manager_backend.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vlad.corp.money_manager_backend.application.participant.AddParticipantUseCase;
import vlad.corp.money_manager_backend.application.participant.ListParticipantsUseCase;
import vlad.corp.money_manager_backend.application.participant.RemoveParticipantUseCase;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;

@Configuration
public class ParticipantUseCaseConfig {

    @Bean
    public AddParticipantUseCase addParticipantUseCase(TripRepository tripRepository) {
        return new AddParticipantUseCase(tripRepository);
    }

    @Bean
    public ListParticipantsUseCase listParticipantsUseCase(TripRepository tripRepository, ParticipantRepository participantRepository) {
        return new ListParticipantsUseCase(tripRepository, participantRepository);
    }

    @Bean
    public RemoveParticipantUseCase removeParticipantUseCase(TripRepository tripRepository) {
        return new RemoveParticipantUseCase(tripRepository);
    }

}
