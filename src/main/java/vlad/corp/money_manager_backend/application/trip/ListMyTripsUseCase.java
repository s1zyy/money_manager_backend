package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import java.util.List;
import java.util.UUID;

public class ListMyTripsUseCase {
    private final TripRepository tripRepository;

    public ListMyTripsUseCase(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public List<Trip> execute(UUID participantId) {
        return tripRepository.findAllTripsForUser(participantId);
}
}
