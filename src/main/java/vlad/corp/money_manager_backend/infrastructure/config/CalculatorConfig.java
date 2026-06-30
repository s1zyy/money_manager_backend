package vlad.corp.money_manager_backend.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vlad.corp.money_manager_backend.application.calculator.CalculateBalancesUseCase;
import vlad.corp.money_manager_backend.application.calculator.CalculateDailyLimitUseCase;
import vlad.corp.money_manager_backend.application.calculator.CalculateSettlementUseCase;

@Configuration
public class CalculatorConfig {

    @Bean
    public CalculateBalancesUseCase calculateBalancesUseCase() {
        return new CalculateBalancesUseCase();
    }

    @Bean
    public CalculateDailyLimitUseCase calculateDailyLimitUseCase() {
        return new CalculateDailyLimitUseCase();
    }

    @Bean
    public CalculateSettlementUseCase calculateSettlementUseCase() {
        return new CalculateSettlementUseCase();
    }
}
