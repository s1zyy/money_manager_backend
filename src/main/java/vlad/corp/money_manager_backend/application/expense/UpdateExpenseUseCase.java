package vlad.corp.money_manager_backend.application.expense;

import vlad.corp.money_manager_backend.application.exceptions.InvalidParticipantException;
import vlad.corp.money_manager_backend.application.exceptions.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Expense;
import vlad.corp.money_manager_backend.domain.model.SplitMode;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.policy.TripAccessPolicy;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import vlad.corp.money_manager_backend.domain.value_objects.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
                           SplitMode newSplitMode,
                           Set<UUID> newParticipantIds,
                           Map<UUID, BigDecimal> newCustomShares,
                           String newDescription,
                           Boolean newIsPrepaid) {

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new NotFoundException("Expense not found"));
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip with id: " + expense.getTripId() + " not found"));

        tripAccessPolicy.ensureCanUpdateExpense(trip, expense, participantId);

        BigDecimal effectiveAmount = newAmount != null ? newAmount : expense.getAmount().amount();
        SplitMode effectiveSplitMode = newSplitMode != null ? newSplitMode : expense.getSplitMode();

        Map<UUID, BigDecimal> shares;
        if (newSplitMode != null || newParticipantIds != null || newCustomShares != null || newAmount != null) {
            shares = buildShares(effectiveSplitMode, newParticipantIds, newCustomShares,
                    effectiveAmount, new HashSet<>(trip.getParticipantIds()), expense);
        } else {
            shares = expense.getParticipantShares();
        }

        Expense updatedExpense = new Expense(
                expense.getId(),
                expense.getTripId(),
                Money.of(effectiveAmount),
                expense.getPayerId(),
                effectiveSplitMode,
                shares,
                newExpenseDate != null ? newExpenseDate : expense.getDate(),
                newDescription != null ? newDescription : expense.getDescription(),
                newIsPrepaid != null ? newIsPrepaid : expense.isPrepaid()
        );
        expenseRepository.save(updatedExpense);
        return updatedExpense;
    }

    private Map<UUID, BigDecimal> buildShares(SplitMode splitMode,
                                               Set<UUID> participantIds,
                                               Map<UUID, BigDecimal> customShares,
                                               BigDecimal total,
                                               Set<UUID> tripParticipants,
                                               Expense existing) {
        if (splitMode == SplitMode.EQUAL) {
            Set<UUID> ids = participantIds != null ? participantIds : existing.getParticipantIds();
            if (!tripParticipants.containsAll(ids)) {
                throw new InvalidParticipantException("Participant is not a member of the trip");
            }
            BigDecimal share = total.divide(BigDecimal.valueOf(ids.size()), 2, RoundingMode.HALF_UP);
            return ids.stream().collect(Collectors.toMap(id -> id, id -> share));
        } else {
            Map<UUID, BigDecimal> shares = customShares != null ? customShares : existing.getParticipantShares();
            if (!tripParticipants.containsAll(shares.keySet())) {
                throw new InvalidParticipantException("Participant is not a member of the trip");
            }
            BigDecimal sum = shares.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            if (sum.setScale(2, RoundingMode.HALF_UP).compareTo(total.setScale(2, RoundingMode.HALF_UP)) != 0) {
                throw new InvalidParticipantException("Custom shares must sum to the total expense amount");
            }
            return new HashMap<>(shares);
        }
    }
}
