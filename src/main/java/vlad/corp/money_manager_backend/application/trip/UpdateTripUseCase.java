package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import vlad.corp.money_manager_backend.domain.value_objects.Money;

import java.util.List;
import java.util.UUID;

public class UpdateTripUseCase {

    private final TripRepository tripRepository;

    public UpdateTripUseCase(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip execute(
            UUID tripId,
            String name,
            Money totalBudget,
            Money prepaidExpenses,
            List<UUID> participantIds
    ) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip not found with id: " + tripId));
        trip.ensureNotArchived();

        Trip updatedTrip = new Trip(
                trip.getId(),
                trip.getOwnerId(),
                name != null ? name : trip.getName(),
                trip.getStartDate(),
                trip.getEndDate(),
                totalBudget != null ? totalBudget : trip.getTotalBudget(),
                prepaidExpenses != null ? prepaidExpenses : trip.getPrepaidExpenses(),
                participantIds != null ? participantIds : trip.getParticipantIds(),
                trip.getExpenseIds(),
                trip.getJoinCode()
        );
        return updatedTrip;

    }
}
