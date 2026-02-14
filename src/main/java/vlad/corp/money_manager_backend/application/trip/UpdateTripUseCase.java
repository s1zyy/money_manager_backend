package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.ExpenseRepository;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import vlad.corp.money_manager_backend.domain.value_objects.Money;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public class UpdateTripUseCase {

    private final TripRepository tripRepository;
    private final ExpenseRepository expenseRepository;

    public UpdateTripUseCase(TripRepository tripRepository, ExpenseRepository expenseRepository) {
        this.tripRepository = tripRepository;
        this.expenseRepository = expenseRepository;
    }

    public Trip execute(
            UUID participantId,
            UUID tripId,
            String name,
            BigDecimal totalBudgetDecimal,
            BigDecimal prepaidExpensesDecimal,
            Set<UUID> participantIds,
            LocalDate startDate,
            LocalDate endDate
    ) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip not found with id: " + tripId));

        trip.ensureOwner(participantId);

        Money totalBudget = totalBudgetDecimal!=null ? new Money(totalBudgetDecimal) : null;
        Money prepaidExpenses = prepaidExpensesDecimal!=null ? new Money(prepaidExpensesDecimal) : null;
        trip.updateBudget(totalBudget, prepaidExpenses);

        String nameReal = name != null ? name : trip.getName();
        trip.updateName(nameReal);

        LocalDate newStart = (startDate != null) ? startDate : trip.getStartDate();
        LocalDate newEnd = (endDate != null) ? endDate : trip.getEndDate();
        if (newEnd.isBefore(newStart)) {
            throw new IllegalArgumentException("Invalid date range");
        }
        trip.updateDates(newStart, newEnd);

        Set<UUID> participantIdSet = expenseRepository.findActiveParticipantIds(tripId);
        Set<UUID> newParticipantIds = participantIds != null ? participantIds : trip.getParticipantIds();
        trip.updateParticipants(newParticipantIds, participantIdSet);

        tripRepository.save(trip);

        return trip;
    }
}
