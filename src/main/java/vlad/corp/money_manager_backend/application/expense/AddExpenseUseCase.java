package vlad.corp.money_manager_backend.application.expense;

import vlad.corp.money_manager_backend.application.exception.InvalidParticipantException;
import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import vlad.corp.money_manager_backend.domain.value_objects.Money;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public class AddExpenseUseCase {
    private final TripRepository tripRepository;
    private final ExpenseRepository expenseRepository;
    private final Clock clock;

    public AddExpenseUseCase(TripRepository tripRepository, ExpenseRepository expenseRepository, Clock clock) {
        this.tripRepository = tripRepository;
        this.expenseRepository = expenseRepository;
        this.clock = clock;
    }

    public Expense execute(UUID tripId, UUID payerId, Money amount, Set<UUID> participants, String description) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip not found"));

        trip.ensureNotArchived();

        if(!trip.getParticipantIds().contains(payerId)) {//todo maybe bring this check to a separate method
            throw new InvalidParticipantException("Payer is not a participant of the trip");
        }

        if(!trip.getParticipantIds().containsAll(participants)) {//todo same here
            throw new InvalidParticipantException("Participant is not a participant of the trip");
        }

        Expense expense =  new Expense(
                UUID.randomUUID(),
                amount,
                payerId,
                participants.stream().toList(),
                LocalDate.now(clock),
                description
        );
        trip.getExpenseIds().add(expense.getId());
        tripRepository.save(trip);
        expenseRepository.save(expense);
        return expense;
    }
}
