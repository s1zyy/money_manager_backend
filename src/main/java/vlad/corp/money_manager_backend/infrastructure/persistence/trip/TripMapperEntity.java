package vlad.corp.money_manager_backend.infrastructure.persistence.trip;

import org.springframework.stereotype.Component;
import vlad.corp.money_manager_backend.domain.model.JoinCode;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.value_objects.Money;
import java.util.HashSet;
import java.util.List;

@Component
public class TripMapperEntity {

    public TripEntity toEntity(Trip trip) {
        return new TripEntity(
                trip.getId(),
                trip.getOwnerId(),
                trip.getName(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getTotalBudget().getAmount(),
                trip.getPrepaidExpenses().getAmount(),
                new HashSet<>(trip.getParticipantIds()),
                trip.getJoinCode().toString(),
                trip.getStatus()
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
                List.copyOf(tripEntity.getParticipantIds()),
                new JoinCode(tripEntity.getJoinCode())
        );
    }
}
