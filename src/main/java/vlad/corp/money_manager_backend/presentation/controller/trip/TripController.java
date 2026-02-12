package vlad.corp.money_manager_backend.presentation.controller.trip;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vlad.corp.money_manager_backend.application.trip.*;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.infrastructure.security.AuthenticatedParticipant;
import vlad.corp.money_manager_backend.presentation.dto.trip.*;
import vlad.corp.money_manager_backend.presentation.mapper.trip_mapper.TripMapperDto;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trips")
public class TripController {
    private final JoinTripUseCase joinTripUseCase;
    private final CreateTripUseCase createTripUseCase;
    private final ListMyTripsUseCase listMyTripsUseCase;
    private final TripMapperDto tripMapperDto;



    public TripController(JoinTripUseCase joinTripUseCase, CreateTripUseCase createTripUseCase, ListMyTripsUseCase listMyTripsUseCase, TripMapperDto tripMapperDto) {
        this.joinTripUseCase = joinTripUseCase;
        this.createTripUseCase = createTripUseCase;
        this.listMyTripsUseCase = listMyTripsUseCase;
        this.tripMapperDto = tripMapperDto;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateTripResult create(
            @AuthenticationPrincipal AuthenticatedParticipant participant,
            @RequestBody CreateTripRequest request
            ) {
        Trip trip = createTripUseCase.execute(
                participant.participantId(),
                request.name(),
                request.totalBudget(),
                request.prepaidExpenses(),
                request.startDate(),
                request.endDate()
        );
        return new CreateTripResult(
                trip.getId(),
                trip.getOwnerId(),
                trip.getName(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getJoinCode().value()
        );

    }

    @PostMapping("/join")
    public UUID join(
            @AuthenticationPrincipal AuthenticatedParticipant participant,
            @RequestBody JoinTripRequest request
    ) {
        return joinTripUseCase.execute(request.code(), participant.participantId());
    }

    @GetMapping
    public List<TripDto> getUserTrips(
            @AuthenticationPrincipal AuthenticatedParticipant participant
    ){
        List<Trip> trips = listMyTripsUseCase.execute(participant.participantId());
        return trips
                .stream()
                .map(tripMapperDto::toDto)
                .toList();
    }


}
