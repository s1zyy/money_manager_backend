package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import vlad.corp.money_manager_backend.domain.value_objects.Money;
import java.math.BigDecimal;
import java.time.LocalDate;
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
            LocalDate endDate,
            String currency

    ) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip not found with id: " + tripId));

        trip.ensureOwner(participantId);

        Money totalBudget = totalBudgetDecimal!=null ? new Money(totalBudgetDecimal) : null;
        Money prepaidExpenses = prepaidExpensesDecimal!=null ? new Money(prepaidExpensesDecimal) : null;
        trip.updateBudget(totalBudget, prepaidExpenses);

        String nameReal = name != null ? name : trip.getName();
        trip.updateName(nameReal);

        LocalDate newEnd = (endDate != null) ? endDate : trip.getEndDate();
        if (newEnd.isBefore(trip.getStartDate())) {
            throw new IllegalArgumentException("Invalid date range");
        }
        trip.updateEndDate(newEnd);


        if(currency != null && !currency.isBlank()) {
            trip.setCurrency(currency);
        }


        tripRepository.save(trip);

        return trip;
    }
}
