package vlad.corp.money_manager_backend.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vlad.corp.money_manager_backend.application.expense.*;
import vlad.corp.money_manager_backend.domain.policy.TripAccessPolicy;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;

@Configuration
public class ExpenseUseCaseConfig {

    @Bean
    public AddExpenseUseCase addExpenseUseCase(TripRepository tripRepository, ExpenseRepository expenseRepository, TripAccessPolicy tripAccessPolicy) {
        return new AddExpenseUseCase(tripRepository, expenseRepository, tripAccessPolicy);
    }

    @Bean
    public DeleteExpenseUseCase deleteExpenseUseCase(TripRepository tripRepository, ExpenseRepository expenseRepository, TripAccessPolicy tripAccessPolicy) {
        return new DeleteExpenseUseCase(tripRepository, expenseRepository, tripAccessPolicy);
    }

    @Bean
    public GetExpenseUseCase getExpenseUseCase(ExpenseRepository expenseRepository, TripRepository tripRepository, TripAccessPolicy tripAccessPolicy) {
        return new GetExpenseUseCase(expenseRepository, tripRepository, tripAccessPolicy);
    }

    @Bean
    public ListExpensesUseCase listExpensesUseCase(ExpenseRepository expenseRepository, TripRepository tripRepository, TripAccessPolicy tripAccessPolicy) {
        return new ListExpensesUseCase(expenseRepository, tripRepository, tripAccessPolicy);
    }

    @Bean
    public UpdateExpenseUseCase updateExpenseUseCase(ExpenseRepository expenseRepository, TripRepository tripRepository, TripAccessPolicy tripAccessPolicy) {
        return new UpdateExpenseUseCase(expenseRepository, tripRepository, tripAccessPolicy);
    }
}
