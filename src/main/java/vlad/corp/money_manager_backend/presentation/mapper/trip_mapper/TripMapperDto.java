package vlad.corp.money_manager_backend.presentation.mapper.trip_mapper;

import org.springframework.stereotype.Component;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.presentation.dto.trip.TripDto;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TripMapperDto {

    public TripDto toDto(Trip trip) {
        Map<UUID, BigDecimal> budgets = trip.getParticipantBudgets().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getAmount()));

        return new TripDto(
                trip.getId(),
                trip.getOwnerId(),
                trip.getName(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getParticipantIds().stream().sorted().toList(),
                budgets,
                trip.getCurrency(),
                trip.getJoinCode().value(),
                trip.getStatus().name()
        );
    }
}
