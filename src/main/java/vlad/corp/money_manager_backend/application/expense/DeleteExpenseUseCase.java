package vlad.corp.money_manager_backend.application.expense;

import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.policy.TripAccessPolicy;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;

import java.util.UUID;

public class DeleteExpenseUseCase {

    private final TripRepository tripRepository;
    private final ExpenseRepository expenseRepository;
    private final TripAccessPolicy tripAccessPolicy;

    public DeleteExpenseUseCase(TripRepository tripRepository, ExpenseRepository expenseRepository, TripAccessPolicy tripAccessPolicy) {
        this.tripRepository = tripRepository;
        this.expenseRepository = expenseRepository;
        this.tripAccessPolicy = tripAccessPolicy;
    }

    public void execute(UUID participantId, UUID expenseId, UUID tripId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new NotFoundException("Expense not found with id: " + expenseId));
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip not found with id: " + tripId));

        tripAccessPolicy.ensureCanDeleteExpense(trip, expense, participantId);

        expenseRepository.delete(expense);
    }
}
