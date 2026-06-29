package vlad.corp.money_manager_backend.application.expense;

import vlad.corp.money_manager_backend.application.exception.InvalidParticipantException;
import vlad.corp.money_manager_backend.application.exception.NotFoundException;
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
                           SplitMode splitMode,
                           Set<UUID> participantIds,
                           Map<UUID, BigDecimal> customShares,
                           String description) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip not found"));

        tripAccessPolicy.ensureNotArchived(trip);
        Money amount = Money.of(amountBigD);

        Set<UUID> tripParticipants = new HashSet<>(trip.getParticipantIds());

        if (payerId != null) {
            tripAccessPolicy.ensureParticipant(trip, payerId);
        }

        Map<UUID, BigDecimal> shares = buildShares(splitMode, participantIds, customShares, amountBigD, tripParticipants);

        Expense expense = new Expense(
                UUID.randomUUID(),
                tripId,
                amount,
                payerId,
                splitMode,
                shares,
                date,
                description
        );
        expenseRepository.save(expense);
        return expense;
    }

    private Map<UUID, BigDecimal> buildShares(SplitMode splitMode,
                                               Set<UUID> participantIds,
                                               Map<UUID, BigDecimal> customShares,
                                               BigDecimal total,
                                               Set<UUID> tripParticipants) {
        if (splitMode == SplitMode.EQUAL) {
            if (participantIds == null || participantIds.isEmpty()) {
                throw new InvalidParticipantException("Participant list is required for EQUAL split");
            }
            if (!tripParticipants.containsAll(participantIds)) {
                throw new InvalidParticipantException("Participant is not a member of the trip");
            }
            BigDecimal share = total.divide(BigDecimal.valueOf(participantIds.size()), 2, RoundingMode.HALF_UP);
            return participantIds.stream().collect(Collectors.toMap(id -> id, id -> share));
        } else {
            if (customShares == null || customShares.isEmpty()) {
                throw new InvalidParticipantException("Custom shares are required for CUSTOM split");
            }
            if (!tripParticipants.containsAll(customShares.keySet())) {
                throw new InvalidParticipantException("Participant is not a member of the trip");
            }
            BigDecimal sum = customShares.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            if (sum.setScale(2, RoundingMode.HALF_UP).compareTo(total.setScale(2, RoundingMode.HALF_UP)) != 0) {
                throw new InvalidParticipantException("Custom shares must sum to the total expense amount");
            }
            return new HashMap<>(customShares);
        }
    }
}
