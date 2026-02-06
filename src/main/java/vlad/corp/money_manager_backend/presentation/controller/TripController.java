package vlad.corp.money_manager_backend.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vlad.corp.money_manager_backend.application.trip.*;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.infrastructure.security.AuthenticatedParticipant;
import vlad.corp.money_manager_backend.presentation.dto.trip.*;
import vlad.corp.money_manager_backend.presentation.mapper.trip_mapper.TripDashboardMapper;
import vlad.corp.money_manager_backend.presentation.mapper.trip_mapper.TripMapperDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trips")
public class TripController {
    private final JoinTripUseCase joinTripUseCase;
    private final GetTripDashboardUseCase getTripDashboardUseCase;
    private final UpdateTripUseCase updateTripUseCase;
    private final CreateTripUseCase createTripUseCase;
    private final ListMyTripsUseCase listMyTripsUseCase;
    private final TripMapperDto tripMapperDto;
    private final TripDashboardMapper tripDashboardMapper;
    private final ArchiveTripUseCase archiveTripUseCase;

    public TripController(JoinTripUseCase joinTripUseCase, GetTripDashboardUseCase getTripDashboardUseCase, UpdateTripUseCase updateTripUseCase, CreateTripUseCase createTripUseCase, ListMyTripsUseCase listMyTripsUseCase, TripMapperDto tripMapperDto, TripDashboardMapper tripDashboardMapper, ArchiveTripUseCase archiveTripUseCase) {
        this.joinTripUseCase = joinTripUseCase;
        this.getTripDashboardUseCase = getTripDashboardUseCase;
        this.updateTripUseCase = updateTripUseCase;
        this.createTripUseCase = createTripUseCase;
        this.listMyTripsUseCase = listMyTripsUseCase;
        this.tripMapperDto = tripMapperDto;
        this.tripDashboardMapper = tripDashboardMapper;
        this.archiveTripUseCase = archiveTripUseCase;
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

    @GetMapping("/{tripId}/dashboard")
    public TripDashboardDto getTripDashboard(
            @AuthenticationPrincipal AuthenticatedParticipant participant,
            @PathVariable UUID tripId
    ){
        TripDashboard dashboard = getTripDashboardUseCase.execute(tripId);
        return tripDashboardMapper.toDto(dashboard);
    }

//    @PutMapping("/{tripId}")
//    public TripDto updateTrip(
//            @AuthenticationPrincipal AuthenticatedParticipant participant,
//            @PathVariable UUID tripId,
//            @RequestBody UpdateTripRequest request) {
//        Trip updatedTrip = updateTripUseCase.execute(
//                tripId,
//                request.name(),
//                request.totalBudget(),
//                request.prepaidExpenses(),
//                request.participantIds()
//    }



}
