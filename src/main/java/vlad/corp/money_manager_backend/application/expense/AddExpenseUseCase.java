package vlad.corp.money_manager_backend.application.expense;

import vlad.corp.money_manager_backend.application.exception.InvalidParticipantException;
import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.policy.TripAccessPolicy;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import vlad.corp.money_manager_backend.domain.value_objects.Money;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AddExpenseUseCase {
    private final TripRepository tripRepository;
    private final ExpenseRepository expenseRepository;
    private final TripAccessPolicy tripAccessPolicy;

    public AddExpenseUseCase(TripRepository tripRepository, ExpenseRepository expenseRepository, TripAccessPolicy tripAccessPolicy) {
        this.tripRepository = tripRepository;
        this.expenseRepository = expenseRepository;
        this.tripAccessPolicy = tripAccessPolicy;
    }

    public Expense execute(UUID tripId,
                           UUID payerId,
                           BigDecimal amountBigD,
                           LocalDate date,
                           Set<UUID> participants,
                           String description) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip not found"));

        tripAccessPolicy.ensureNotArchived(trip);
        Money amount =  Money.of(amountBigD);

        tripAccessPolicy.ensureParticipant(trip, payerId);

        if(!new HashSet<>(trip.getParticipantIds()).containsAll(participants)) {
            throw new InvalidParticipantException("Participant is not a participant of the trip");
        }

        Expense expense =  new Expense(
                UUID.randomUUID(),
                tripId,
                amount,
                payerId,
                participants,
                date,
                description
        );
        expenseRepository.save(expense);
        return expense;
    }
}
