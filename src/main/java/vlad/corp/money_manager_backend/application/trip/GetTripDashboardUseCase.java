package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.application.calculator.CalculateBalancesUseCase;
import vlad.corp.money_manager_backend.application.calculator.CalculateDailyLimitUseCase;
import vlad.corp.money_manager_backend.application.exceptions.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import vlad.corp.money_manager_backend.domain.value_objects.Money;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GetTripDashboardUseCase {

    private final TripRepository tripRepository;
    private final ExpenseRepository expenseRepository;
    private final CalculateDailyLimitUseCase calculateDailyLimitUseCase;
    private final CalculateBalancesUseCase calculateBalancesUseCase;

    public GetTripDashboardUseCase(TripRepository tripRepository, ExpenseRepository expenseRepository,
                                   CalculateDailyLimitUseCase calculateDailyLimitUseCase,
                                   CalculateBalancesUseCase calculateBalancesUseCase) {
        this.tripRepository = tripRepository;
        this.expenseRepository = expenseRepository;
        this.calculateDailyLimitUseCase = calculateDailyLimitUseCase;
        this.calculateBalancesUseCase = calculateBalancesUseCase;
    }

    public TripDashboard execute(UUID tripId, UUID participantId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip not found"));

        trip.ensureParticipant(participantId);

        List<Expense> expenses = expenseRepository.findAllByTripId(tripId);

        Money myBudget = trip.getMyBudget(participantId);
        Money myDailyLimit = calculateDailyLimitUseCase.execute(participantId, trip, expenses);

        LocalDate today = LocalDate.now();
        Money mySpentToday = expenses.stream()
                .filter(e -> !e.isPrepaid() && e.getDate() != null && e.getDate().equals(today))
                .map(e -> CalculateDailyLimitUseCase.getMyShare(participantId, e))
                .reduce(Money.zero(), Money::add);

        Map<UUID, Money> balances = calculateBalancesUseCase.execute(trip, expenses);

        boolean isOwner = trip.getOwnerId().equals(participantId);
        boolean involvedInExpenses = expenses.stream().anyMatch(e ->
                (e.getPayerId() != null && e.getPayerId().equals(participantId))
                        || e.getParticipantIds().contains(participantId)
        );
        boolean canLeave = !isOwner && !involvedInExpenses;

        return new TripDashboard(trip, participantId, myBudget, myDailyLimit, mySpentToday, balances, expenses, isOwner, canLeave);
    }
}
