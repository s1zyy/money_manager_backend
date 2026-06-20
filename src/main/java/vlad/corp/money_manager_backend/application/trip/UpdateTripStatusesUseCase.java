package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.model.TripStatus;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;

import java.time.LocalDate;
import java.util.List;

public class UpdateTripStatusesUseCase {
    private final TripRepository tripRepository;

    public UpdateTripStatusesUseCase(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public void execute() {
        LocalDate today = LocalDate.now();

        List<Trip> upcomingToActive = tripRepository.findUpcomingTripsStartingByDate(today);

        for(Trip trip: upcomingToActive) {
            trip.setStatus(TripStatus.ACTIVE);
            tripRepository.save(trip);
        }
    }
}
