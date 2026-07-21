package vlad.corp.money_manager_backend.application.expense;

import vlad.corp.money_manager_backend.application.exceptions.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.policy.TripAccessPolicy;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import java.util.UUID;

public class GetExpenseUseCase {
    private final ExpenseRepository expenseRepository;
    private final TripRepository tripRepository;
    private final TripAccessPolicy tripAccessPolicy;

    public GetExpenseUseCase(ExpenseRepository expenseRepository, TripRepository tripRepository, TripAccessPolicy tripAccessPolicy) {
        this.expenseRepository = expenseRepository;
        this.tripRepository = tripRepository;
        this.tripAccessPolicy = tripAccessPolicy;
    }

    public Expense execute(UUID participantId, UUID expenseId, UUID tripId) {
        Expense expense =  expenseRepository.findById(expenseId)
                .orElseThrow(() -> new NotFoundException("Expense not found with id: " + expenseId));
        if(!expense.getTripId().equals(tripId)) {
            throw new NotFoundException("Expense not found in this trip");
        }
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip not found with id: " + tripId));
        tripAccessPolicy.ensureNotArchived(trip);
        tripAccessPolicy.ensureParticipant(trip, participantId);
        return expense;
    }
}
