package vlad.corp.money_manager_backend.application.expense;

import vlad.corp.money_manager_backend.application.exceptions.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.policy.TripAccessPolicy;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import java.util.List;
import java.util.UUID;

public class ListExpensesUseCase {

    private final ExpenseRepository expenseRepository;
    private final TripRepository tripRepository;
    private final TripAccessPolicy tripAccessPolicy;

    public ListExpensesUseCase(ExpenseRepository expenseRepository, TripRepository tripRepository, TripAccessPolicy tripAccessPolicy) {
        this.expenseRepository = expenseRepository;
        this.tripRepository = tripRepository;
        this.tripAccessPolicy = tripAccessPolicy;
    }

    public List<Expense> execute(UUID tripId, UUID participantId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip with id: " + tripId + " not found"));
        tripAccessPolicy.ensureNotArchived(trip);
        tripAccessPolicy.ensureParticipant(trip, participantId);

        return expenseRepository.findAllByTripId(tripId);
    }
}
