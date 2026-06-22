package vlad.corp.money_manager_backend.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vlad.corp.money_manager_backend.application.calculator.CalculateBalancesUseCase;
import vlad.corp.money_manager_backend.application.calculator.CalculateDailyLimitUseCase;
import vlad.corp.money_manager_backend.application.port.JoinCodeGenerator;
import vlad.corp.money_manager_backend.application.trip.*;
import vlad.corp.money_manager_backend.domain.policy.TripAccessPolicy;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;

@Configuration
public class TripUseCaseConfig {

    @Bean
    public TripAccessPolicy tripAccessPolicy() {
        return new TripAccessPolicy();
    }

    @Bean
    public ListParticipantsUseCase listParticipantsUseCase(TripRepository tripRepository, ParticipantRepository participantRepository) {
        return new ListParticipantsUseCase(tripRepository, participantRepository);
    }

    @Bean
    public LeaveTripUseCase leaveTripUseCase(TripRepository tripRepository) {
        return new LeaveTripUseCase(tripRepository);
    }

    @Bean
    public ArchiveTripUseCase archiveTripUseCase(TripRepository tripRepository) {
        return new ArchiveTripUseCase(tripRepository);
    }

    @Bean
    public CreateTripUseCase createTripUseCase(TripRepository tripRepository, JoinCodeGenerator joinCodeGenerator) {
        return new CreateTripUseCase(tripRepository, joinCodeGenerator);
    }

    @Bean
    public GetTripDashboardUseCase getTripDashboardUseCase(TripRepository tripRepository, ExpenseRepository expenseRepository, CalculateDailyLimitUseCase calculateDailyLimitUseCase, CalculateBalancesUseCase calculateBalancesUseCase) {
        return new GetTripDashboardUseCase(tripRepository,
                expenseRepository,
                calculateDailyLimitUseCase,
                calculateBalancesUseCase);
    }

    @Bean
    public JoinTripUseCase joinTripUseCase(TripRepository tripRepository) {
        return new JoinTripUseCase(tripRepository);
    }

    @Bean
    public UpdateTripUseCase updateTripUseCase(TripRepository tripRepository) {
        return new UpdateTripUseCase(tripRepository);
    }

    @Bean
    public ListMyTripsUseCase listMyTripsUseCase(TripRepository tripRepository) {
        return new ListMyTripsUseCase(tripRepository);
    }

    @Bean
    public GetTripUseCase getTripUseCase(TripRepository tripRepository) {
        return new GetTripUseCase(tripRepository);
    }

    @Bean
    public UpdateTripStatusesUseCase updateTripStatusesUseCase(TripRepository tripRepository) { return new UpdateTripStatusesUseCase(tripRepository); }
}
