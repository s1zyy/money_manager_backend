package vlad.corp.money_manager_backend.presentation.controller.trip;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vlad.corp.money_manager_backend.application.trip.*;
import vlad.corp.money_manager_backend.domain.model.Trip;
import vlad.corp.money_manager_backend.infrastructure.security.AuthenticatedParticipant;
import vlad.corp.money_manager_backend.presentation.dto.trip.TripDashboardDto;
import vlad.corp.money_manager_backend.presentation.dto.trip.TripDto;
import vlad.corp.money_manager_backend.presentation.dto.trip.UpdateTripRequest;
import vlad.corp.money_manager_backend.presentation.mapper.trip_mapper.TripDashboardMapper;
import vlad.corp.money_manager_backend.presentation.mapper.trip_mapper.TripMapperDto;

import java.util.UUID;

@RestController
@RequestMapping("/api/trips")
public class TripManagementController {

    private final GetTripDashboardUseCase getTripDashboardUseCase;
    private final UpdateTripUseCase updateTripUseCase;
    private final TripDashboardMapper tripDashboardMapper;
    private final ArchiveTripUseCase archiveTripUseCase;
    private final LeaveTripUseCase leaveTripUseCase;
    private final GetTripUseCase getTripUseCase;
    private final TripMapperDto tripMapperDto;



    public TripManagementController(GetTripDashboardUseCase getTripDashboardUseCase, UpdateTripUseCase updateTripUseCase, TripDashboardMapper tripDashboardMapper, ArchiveTripUseCase archiveTripUseCase, LeaveTripUseCase leaveTripUseCase, GetTripUseCase getTripUseCase, TripMapperDto tripMapperDto) {
        this.getTripDashboardUseCase = getTripDashboardUseCase;
        this.updateTripUseCase = updateTripUseCase;
        this.tripDashboardMapper = tripDashboardMapper;
        this.archiveTripUseCase = archiveTripUseCase;
        this.leaveTripUseCase = leaveTripUseCase;
        this.getTripUseCase = getTripUseCase;
        this.tripMapperDto = tripMapperDto;
    }


    @PostMapping("/{tripId}/archive")
    public TripDto archiveTrip(
            @AuthenticationPrincipal AuthenticatedParticipant participant,
            @PathVariable UUID tripId
    ) {
        Trip trip =  archiveTripUseCase.execute(tripId, participant.participantId());
        return tripMapperDto.toDto(trip);
    }

    @PostMapping("/{tripId}/leave")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void leaveTrip(
            @AuthenticationPrincipal AuthenticatedParticipant participant,
            @PathVariable UUID tripId
    ) {
        leaveTripUseCase.execute(tripId, participant.participantId());
    }


    @GetMapping("/{tripId}/dashboard")
    public TripDashboardDto getTripDashboard(
            @AuthenticationPrincipal AuthenticatedParticipant participant,
            @PathVariable UUID tripId
    ){
        TripDashboard dashboard = getTripDashboardUseCase.execute(tripId, participant.participantId());
        return tripDashboardMapper.toDto(dashboard);
    }

    @GetMapping("/{tripId}")
    public TripDto getTrip(
            @AuthenticationPrincipal AuthenticatedParticipant participant,
            @PathVariable UUID tripId
    ) {
        Trip trip =  getTripUseCase.execute(tripId, participant.participantId());
        return tripMapperDto.toDto(trip);
    }

    @PutMapping("/{tripId}")
    public TripDto updateTrip(
            @AuthenticationPrincipal AuthenticatedParticipant participant,
            @PathVariable UUID tripId,
            @Valid @RequestBody UpdateTripRequest request) {
        Trip updatedTrip = updateTripUseCase.execute(
                participant.participantId(),
                tripId,
                request.name(),
                request.budget(),
                request.prepaidExpenses(),
                request.endDate(),
                request.currency());
        return tripMapperDto.toDto(updatedTrip);
    }
}
