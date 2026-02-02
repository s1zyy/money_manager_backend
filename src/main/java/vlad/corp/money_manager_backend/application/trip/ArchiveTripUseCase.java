package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;

import java.util.UUID;

public class ArchiveTripUseCase {

    private final TripRepository tripRepository;

    public ArchiveTripUseCase(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public void execute(UUID tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip not found with id: " + tripId));

        trip.archive();
        tripRepository.save(trip);
    }
}
