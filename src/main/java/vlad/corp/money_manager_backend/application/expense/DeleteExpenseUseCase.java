package vlad.corp.money_manager_backend.application.expense;

import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;

import java.util.UUID;

public class DeleteExpenseUseCase {

    private final TripRepository tripRepository;
    private final ExpenseRepository expenseRepository;

    public DeleteExpenseUseCase(TripRepository tripRepository, ExpenseRepository expenseRepository) {
        this.tripRepository = tripRepository;
        this.expenseRepository = expenseRepository;
    }

    public void execute(UUID tripId, UUID expenseId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip not found with id: " + tripId));

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new NotFoundException("Expense not found with id: " + expenseId));

        trip.getExpenseIds().remove(expenseId);
        tripRepository.save(trip);
        expenseRepository.delete(expense);
    }
}
