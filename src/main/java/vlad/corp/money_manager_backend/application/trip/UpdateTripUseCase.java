package vlad.corp.money_manager_backend.application.trip;

import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.exceptions.BusinessException;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.model.TripStatus;
import vlad.corp.money_manager_backend.domain.repository.TripRepository;
import java.time.LocalDate;
import java.util.UUID;

public class UpdateTripUseCase {

    private final TripRepository tripRepository;

    public UpdateTripUseCase(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip execute(UUID participantId, UUID tripId, String name,
                        LocalDate startDate, LocalDate endDate, String currency) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip not found with id: " + tripId));

        trip.ensureOwner(participantId);
        trip.updateName(name != null ? name : trip.getName());

        if (currency != null && !currency.isBlank()) {
            trip.setCurrency(currency);
        }

        applyDateConstraints(trip, startDate, endDate);

        tripRepository.save(trip);
        return trip;
    }

    private void applyDateConstraints(Trip trip, LocalDate startDate, LocalDate endDate) {
        if (trip.getStatus() == TripStatus.UPCOMING) {
            LocalDate newStart = startDate != null ? startDate : trip.getStartDate();
            LocalDate newEnd = endDate != null ? endDate : trip.getEndDate();
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            LocalDate minEnd = newStart.plusDays(1).isAfter(tomorrow) ? newStart.plusDays(1) : tomorrow;
            if (newEnd.isBefore(minEnd)) {
                throw new BusinessException("End date must be at least tomorrow");
            }
            if (!newStart.isAfter(LocalDate.now())) {
                trip.setStatus(TripStatus.ACTIVE);
            }
            trip.updateStartDate(newStart);
            trip.updateEndDate(newEnd);
        } else if (trip.getStatus() == TripStatus.ACTIVE) {
            if (startDate != null && !startDate.equals(trip.getStartDate())) {
                throw new BusinessException("Cannot change start date of an active trip");
            }
            if (endDate != null) {
                if (!endDate.isAfter(LocalDate.now())) {
                    throw new BusinessException("End date must be at least tomorrow");
                }
                trip.updateEndDate(endDate);
            }
        }
    }
}
