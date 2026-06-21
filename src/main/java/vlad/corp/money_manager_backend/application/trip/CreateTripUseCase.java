package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.application.port.JoinCodeGenerator;
import vlad.corp.money_manager_backend.domain.model.JoinCode;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.model.TripStatus;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import vlad.corp.money_manager_backend.domain.value_objects.Money;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class CreateTripUseCase {
    private final TripRepository tripRepository;
    private final JoinCodeGenerator joinCodeGenerator;

    public CreateTripUseCase(TripRepository tripRepository, JoinCodeGenerator joinCodeGenerator) {
        this.tripRepository = tripRepository;
        this.joinCodeGenerator = joinCodeGenerator;
    }

    public Trip execute(
            UUID ownerId,
            String name,
            BigDecimal totalBudget,
            BigDecimal prepaidExpenses,
            LocalDate startDate,
            LocalDate endDate,
            String currency
    ) {
        JoinCode joinCode = joinCodeGenerator.generate();
        Money totalBudgetMoney = new Money(totalBudget);
        Money prepaidExpensesMoney = new Money(prepaidExpenses);


        Set<UUID> participantIds = new HashSet<>();
        participantIds.add(ownerId);

        TripStatus initialStatus = determineInitialStatus(startDate);

        Trip trip = new Trip(
                UUID.randomUUID(),
                ownerId,
                name,
                startDate,
                endDate,
                totalBudgetMoney,
                prepaidExpensesMoney,
                participantIds,
                joinCode,
                initialStatus,
                currency

        );
        tripRepository.save(trip);
        return trip;

    }

    private TripStatus determineInitialStatus(LocalDate startDate) {
        if(startDate == null) {
            return TripStatus.ACTIVE;
        }
        LocalDate now = LocalDate.now();
        if(startDate.isAfter(now)) {
            return TripStatus.UPCOMING;
        }
        return TripStatus.ACTIVE;
    }
}
