package vlad.corp.money_manager_backend.application.expense;

import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import vlad.corp.money_manager_backend.domain.value_objects.Money;
import java.util.List;
import java.util.UUID;

public class UpdateExpenseUseCase {

    private final ExpenseRepository expenseRepository;

    public UpdateExpenseUseCase(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense execute(UUID expenseId, Money newAmount, List<UUID> newParticipants,
                           String newDescription) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new NotFoundException("Expense not found"));

        Expense updatedExpense = new Expense(
                expense.getId(),
                newAmount != null ? newAmount : expense.getAmount(),
                expense.getPayerId(),
                newParticipants != null ? newParticipants : expense.getParticipantIds(),
                expense.getDate(),
                newDescription != null ? newDescription : expense.getDescription()
        );
        expenseRepository.save(updatedExpense);
        return updatedExpense;

    }
}
