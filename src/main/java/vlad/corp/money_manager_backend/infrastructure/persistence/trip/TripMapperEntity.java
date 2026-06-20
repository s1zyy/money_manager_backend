package vlad.corp.money_manager_backend.infrastructure.persistence.trip;

import org.springframework.stereotype.Component;
import vlad.corp.money_manager_backend.domain.model.JoinCode;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.model.TripStatus;
import vlad.corp.money_manager_backend.domain.value_objects.Money;
import vlad.corp.money_manager_backend.infrastructure.persistence.trip.status.TripStatusEntity;

import java.util.HashSet;

@Component
public class TripMapperEntity {



    public TripEntity toEntity(Trip trip) {

        Long statusId = mapStatusToId(trip.getStatus());
        return new TripEntity(
                trip.getId(),
                trip.getOwnerId(),
                trip.getName(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getTotalBudget().getAmount(),
                trip.getPrepaidExpenses().getAmount(),
                new HashSet<>(trip.getParticipantIds()),
                trip.getJoinCode().value(),
                new TripStatusEntity(statusId, trip.getStatus())
        );
    }
    public Trip toDomain(TripEntity tripEntity) {
        return new Trip(
                tripEntity.getId(),
                tripEntity.getOwnerId(),
                tripEntity.getName(),
                tripEntity.getStartDate(),
                tripEntity.getEndDate(),
                new Money(tripEntity.getTotalBudget()),
                new Money(tripEntity.getPrepaidExpenses()),
                new HashSet<>(tripEntity.getParticipantIds()),
                new JoinCode(tripEntity.getJoinCode()),
                tripEntity.getTripStatus().getCode()
        );
    }

    private Long mapStatusToId(TripStatus status) {
        return switch(status) {
            case UPCOMING -> 1L;
            case ACTIVE -> 2L;
            case ARCHIVED -> 3L;
        };

    }
}
