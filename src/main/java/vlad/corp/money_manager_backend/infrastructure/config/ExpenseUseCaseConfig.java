package vlad.corp.money_manager_backend.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vlad.corp.money_manager_backend.application.expense.*;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;

import java.time.Clock;

@Configuration
public class ExpenseUseCaseConfig {

    @Bean
    public AddExpenseUseCase addExpenseUseCase(TripRepository tripRepository, ExpenseRepository expenseRepository, Clock clock) {
        return new AddExpenseUseCase(tripRepository, expenseRepository, clock);
    }

    @Bean
    public DeleteExpenseUseCase deleteExpenseUseCase(TripRepository tripRepository, ExpenseRepository expenseRepository) {
        return new DeleteExpenseUseCase(tripRepository, expenseRepository);
    }

    @Bean
    public GetExpenseUseCase getExpenseUseCase(ExpenseRepository expenseRepository) {
        return new GetExpenseUseCase(expenseRepository);
    }

    @Bean
    public ListExpensesUseCase listExpensesUseCase(ExpenseRepository expenseRepository) {
        return new ListExpensesUseCase(expenseRepository);
    }

    @Bean
    public UpdateExpenseUseCase updateExpenseUseCase(ExpenseRepository expenseRepository) {
        return new UpdateExpenseUseCase(expenseRepository);
    }
}
