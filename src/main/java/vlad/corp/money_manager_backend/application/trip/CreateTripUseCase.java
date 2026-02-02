package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.domain.model.JoinCode;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import vlad.corp.money_manager_backend.domain.value_objects.Money;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateTripUseCase {
    private final TripRepository tripRepository;

    public CreateTripUseCase(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip execute(
            String name,
            Money totalBudget,
            Money prepaidExpenses,
            UUID creatorId,
            LocalDate startDate,
            LocalDate endDate,
            String joinCode
    ) {

        List<UUID> participantIds = new ArrayList<>();
        participantIds.add(creatorId);

        List<UUID> expenseIds = new ArrayList<>();

        Trip trip = new Trip(
                UUID.randomUUID(),
                creatorId,
                name,
                startDate,
                endDate,
                totalBudget,
                prepaidExpenses,
                participantIds,
                expenseIds,
                new JoinCode(joinCode)

        );

        tripRepository.save(trip);
        return trip;
    }
}
