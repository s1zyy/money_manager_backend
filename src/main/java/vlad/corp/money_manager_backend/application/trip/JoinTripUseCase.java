package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.application.exceptions.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.JoinCode;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import vlad.corp.money_manager_backend.domain.value_objects.Money;
import java.math.BigDecimal;
import java.util.UUID;

public class JoinTripUseCase {
    private final TripRepository tripRepository;

    public JoinTripUseCase(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip execute(String code, UUID participantId, BigDecimal budget) {
        JoinCode joinCode = new JoinCode(code);
        Trip trip = tripRepository.findByJoinCode(joinCode)
                .orElseThrow(() -> new NotFoundException("Trip not found with join code: " + code));
        trip.addParticipant(participantId, new Money(budget));
        tripRepository.save(trip);
        return trip;
    }
}
