package vlad.corp.money_manager_backend.infrastructure.persistence.trip;

import org.springframework.stereotype.Component;
import vlad.corp.money_manager_backend.domain.model.JoinCode;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.domain.model.TripStatus;
import vlad.corp.money_manager_backend.domain.value_objects.Money;
import vlad.corp.money_manager_backend.infrastructure.persistence.trip.status.TripStatusEntity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class TripMapperEntity {

    public TripEntity toEntity(Trip trip) {
        Map<UUID, BigDecimal> budgets = new HashMap<>();
        trip.getParticipantBudgets().forEach((id, money) -> budgets.put(id, money.getAmount()));

        Long statusId = mapStatusToId(trip.getStatus());
        return new TripEntity(
                trip.getId(),
                trip.getOwnerId(),
                trip.getName(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getCurrency(),
                budgets,
                trip.getJoinCode().value(),
                new TripStatusEntity(statusId, trip.getStatus())
        );
    }

    public Trip toDomain(TripEntity tripEntity) {
        Map<UUID, Money> budgets = new HashMap<>();
        tripEntity.getParticipantBudgets().forEach((id, amount) -> budgets.put(id, new Money(amount)));

        return new Trip(
                tripEntity.getId(),
                tripEntity.getOwnerId(),
                tripEntity.getName(),
                tripEntity.getStartDate(),
                tripEntity.getEndDate(),
                budgets,
                new JoinCode(tripEntity.getJoinCode()),
                tripEntity.getTripStatus().getCode(),
                tripEntity.getCurrency()
        );
    }

    private Long mapStatusToId(TripStatus status) {
        return switch (status) {
            case UPCOMING -> 1L;
            case ACTIVE -> 2L;
            case ARCHIVED -> 3L;
        };
    }
}
