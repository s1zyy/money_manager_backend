package vlad.corp.money_manager_backend.application.expense;

import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.policy.TripAccessPolicy;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import vlad.corp.money_manager_backend.domain.value_objects.Money;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public class UpdateExpenseUseCase {

    private final ExpenseRepository expenseRepository;
    private final TripRepository tripRepository;
    private final TripAccessPolicy tripAccessPolicy;

    public UpdateExpenseUseCase(ExpenseRepository expenseRepository, TripRepository tripRepository, TripAccessPolicy tripAccessPolicy) {
        this.expenseRepository = expenseRepository;
        this.tripRepository = tripRepository;
        this.tripAccessPolicy = tripAccessPolicy;
    }

    public Expense execute(UUID participantId,
                           UUID expenseId,
                           UUID tripId,
                           LocalDate newExpenseDate,
                           BigDecimal newAmount,
                           Set<UUID> newParticipants,
                           String newDescription) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new NotFoundException("Expense not found"));
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip with id: " + expense.getTripId() + " not found"));

        tripAccessPolicy.ensureCanUpdateExpense(trip, expense, participantId);

        Expense updatedExpense = new Expense(
                expense.getId(),
                expense.getTripId(),
                newAmount != null ? Money.of(newAmount) : expense.getAmount(),
                expense.getPayerId(),
                newParticipants != null ? newParticipants : expense.getParticipantIds(),
                newExpenseDate != null ? newExpenseDate : expense.getDate(),
                newDescription != null ? newDescription : expense.getDescription()
        );
        expenseRepository.save(updatedExpense);
        return updatedExpense;

    }
}
