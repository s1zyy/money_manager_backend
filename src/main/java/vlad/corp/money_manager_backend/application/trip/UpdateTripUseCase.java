package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import vlad.corp.money_manager_backend.domain.value_objects.Money;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class UpdateTripUseCase {

    private final TripRepository tripRepository;

    public UpdateTripUseCase(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip execute(
            UUID participantId,
            UUID tripId,
            String name,
            BigDecimal totalBudgetDecimal,
            BigDecimal prepaidExpensesDecimal,
            List<UUID> participantIds,
            LocalDate startDate,
            LocalDate endDate
    ) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip not found with id: " + tripId));

        trip.ensureParticipant(participantId);

        Money totalBudget = totalBudgetDecimal==null ? null : new Money(totalBudgetDecimal);
        Money prepaidExpenses = prepaidExpensesDecimal==null ? null : new Money(prepaidExpensesDecimal);


        trip.updateName(name);
        trip.updateDates(startDate, endDate);
        trip.updateBudget(totalBudget, prepaidExpenses);
        trip.updateParticipants(participantIds);

        tripRepository.save(trip);

        return trip;
    }
}
