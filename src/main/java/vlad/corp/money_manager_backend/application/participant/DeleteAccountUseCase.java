package vlad.corp.money_manager_backend.application.participant;

import vlad.corp.money_manager_backend.application.calculator.CalculateBalancesUseCase;
import vlad.corp.money_manager_backend.domain.exceptions.BusinessException;
import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.model.TripStatus;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import vlad.corp.money_manager_backend.domain.value_objects.Money;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DeleteAccountUseCase {

    private final TripRepository tripRepository;
    private final ExpenseRepository expenseRepository;
    private final CalculateBalancesUseCase calculateBalancesUseCase;
    private final ParticipantRepository participantRepository;

    public DeleteAccountUseCase(TripRepository tripRepository,
                                ExpenseRepository expenseRepository,
                                CalculateBalancesUseCase calculateBalancesUseCase,
                                ParticipantRepository participantRepository) {
        this.tripRepository = tripRepository;
        this.expenseRepository = expenseRepository;
        this.calculateBalancesUseCase = calculateBalancesUseCase;
        this.participantRepository = participantRepository;
    }

    public void execute(UUID participantId) {
        List<Trip> allTrips = tripRepository.findAllTripsForUser(participantId);

        List<Trip> activeTrips = allTrips.stream()
                .filter(t -> t.getStatus() != TripStatus.ARCHIVED)
                .toList();

        // Условие 1: нельзя удалить аккаунт если ты владелец активной поездки
        boolean ownsActiveTrip = activeTrips.stream()
                .anyMatch(t -> t.getOwnerId().equals(participantId));
        if (ownsActiveTrip) {
            throw new BusinessException("You own active trips. Archive them before deleting your account.");
        }

        // Условие 2: нельзя удалить если есть неурегулированный баланс в активной поездке
        for (Trip trip : activeTrips) {
            List<Expense> expenses = expenseRepository.findAllByTripId(trip.getId());
            Map<UUID, Money> balances = calculateBalancesUseCase.execute(trip, expenses);
            Money myBalance = balances.getOrDefault(participantId, Money.zero());

            if (myBalance.amount().compareTo(java.math.BigDecimal.ZERO) != 0) {
                throw new BusinessException("You have unsettled debts in active trips. Settle them before deleting your account.");
            }
        }

        participantRepository.softDelete(participantId);
    }
}
