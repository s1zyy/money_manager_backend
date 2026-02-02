package vlad.corp.money_manager_backend.application.expense;

import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;

import java.util.UUID;

public class GetExpenseUseCase {
    private final ExpenseRepository expenseRepository;

    public GetExpenseUseCase(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense execute(UUID expenseId) {
        return expenseRepository.findById(expenseId)
                .orElseThrow(() -> new NotFoundException("Expense not found with id: " + expenseId));
    }
}
