package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.application.port.JoinCodeGenerator;
import vlad.corp.money_manager_backend.domain.model.JoinCode;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import vlad.corp.money_manager_backend.domain.value_objects.Money;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
            LocalDate endDate
    ) {
        JoinCode joinCode = joinCodeGenerator.generate();
        Money totalBudgetMoney = new Money(totalBudget);
        Money prepaidExpensesMoney = new Money(prepaidExpenses);


        List<UUID> participantIds = new ArrayList<>();
        participantIds.add(ownerId);

        Trip trip = new Trip(
                UUID.randomUUID(),
                ownerId,
                name,
                startDate,
                endDate,
                totalBudgetMoney,
                prepaidExpensesMoney,
                participantIds,
                joinCode
        );
        tripRepository.save(trip);
        return trip;

    }
}
