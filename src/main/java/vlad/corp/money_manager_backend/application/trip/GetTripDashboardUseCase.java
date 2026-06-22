package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.application.calculator.CalculateBalancesUseCase;
import vlad.corp.money_manager_backend.application.calculator.CalculateDailyLimitUseCase;
import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import vlad.corp.money_manager_backend.domain.value_objects.Money;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GetTripDashboardUseCase {

    private final TripRepository tripRepository;
    private final ExpenseRepository expenseRepository;
    private final CalculateDailyLimitUseCase calculateDailyLimitUseCase;
    private final CalculateBalancesUseCase calculateBalancesUseCase;

    public GetTripDashboardUseCase(TripRepository tripRepository, ExpenseRepository expenseRepository, CalculateDailyLimitUseCase calculateDailyLimitUseCase, CalculateBalancesUseCase calculateBalancesUseCase) {
        this.tripRepository = tripRepository;
        this.expenseRepository = expenseRepository;
        this.calculateDailyLimitUseCase = calculateDailyLimitUseCase;
        this.calculateBalancesUseCase = calculateBalancesUseCase;
    }

    public TripDashboard execute (UUID tripId, UUID participantId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip not found"));

        trip.ensureParticipant(participantId);

        List<Expense> expenses = expenseRepository.findAllByTripId(tripId);

        Money dailyLimit = calculateDailyLimitUseCase.execute(trip, expenses);

        Map<UUID, Money> balances = calculateBalancesUseCase.execute(trip, expenses);

        boolean isOwner = trip.getOwnerId().equals(participantId);
        boolean involvedInExpenses = expenses.stream().anyMatch(e->
                 e.getPayerId().equals(participantId) || e.getParticipantIds().contains(participantId)
        );
        boolean canLeave = !isOwner && !involvedInExpenses;

        return new TripDashboard(
                trip,
                dailyLimit,
                balances,
                expenses,
                isOwner,
                canLeave
        );
    }

}
