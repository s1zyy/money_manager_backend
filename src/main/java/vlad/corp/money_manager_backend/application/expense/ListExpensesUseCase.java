package vlad.corp.money_manager_backend.application.expense;

import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;

import java.util.List;
import java.util.UUID;

public class ListExpensesUseCase {

    private final ExpenseRepository expenseRepository;

    public ListExpensesUseCase(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> execute(UUID tripId) {
        return expenseRepository.findAllByTripId(tripId);
    }
}
