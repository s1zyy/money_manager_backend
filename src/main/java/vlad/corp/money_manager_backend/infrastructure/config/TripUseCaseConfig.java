package vlad.corp.money_manager_backend.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vlad.corp.money_manager_backend.application.calculator.CalculateBalancesUseCase;
import vlad.corp.money_manager_backend.application.calculator.CalculateDailyLimitUseCase;
import vlad.corp.money_manager_backend.application.trip.*;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;

@Configuration
public class TripUseCaseConfig {

    @Bean
    public ArchiveTripUseCase archiveTripUseCase(TripRepository tripRepository) {
        return new ArchiveTripUseCase(tripRepository);
    }

    @Bean
    public CreateTripUseCase createTripUseCase(TripRepository tripRepository) {
        return new CreateTripUseCase(tripRepository);
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
}
