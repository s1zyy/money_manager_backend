package vlad.corp.money_manager_backend.presentation.mapper.trip_mapper;

import org.springframework.stereotype.Component;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.presentation.dto.trip.TripDto;

@Component
public class TripMapperDto {

    public TripDto toDto(Trip trip) {
        return new TripDto(
                trip.getId(),
                trip.getOwnerId(),
                trip.getName(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getParticipantIds(),
                trip.getJoinCode().value(),
                trip.getStatus().name()

        );
    }
}
