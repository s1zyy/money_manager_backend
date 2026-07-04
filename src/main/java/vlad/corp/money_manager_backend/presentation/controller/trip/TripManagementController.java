package vlad.corp.money_manager_backend.presentation.controller.trip;

import jakarta.validation.Valid;
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
public class TripManagementController {

    private final GetTripDashboardUseCase getTripDashboardUseCase;
    private final UpdateTripUseCase updateTripUseCase;
    private final UpdateParticipantBudgetUseCase updateParticipantBudgetUseCase;
    private final TripDashboardMapper tripDashboardMapper;
    private final ArchiveTripUseCase archiveTripUseCase;
    private final GetTripUseCase getTripUseCase;
    private final TripMapperDto tripMapperDto;
    private final DeleteTripUseCase deleteTripUseCase;
    private final GetTripSettlementUseCase getTripSettlementUseCase;
    private final UnarchiveTripUseCase unarchiveTripUseCase;

    public TripManagementController(GetTripDashboardUseCase getTripDashboardUseCase,
                                    UpdateTripUseCase updateTripUseCase,
                                    UpdateParticipantBudgetUseCase updateParticipantBudgetUseCase,
                                    TripDashboardMapper tripDashboardMapper,
                                    ArchiveTripUseCase archiveTripUseCase,
                                    GetTripUseCase getTripUseCase,
                                    TripMapperDto tripMapperDto,
                                    DeleteTripUseCase deleteTripUseCase,
                                    GetTripSettlementUseCase getTripSettlementUseCase,
                                    UnarchiveTripUseCase unarchiveTripUseCase) {
        this.getTripDashboardUseCase = getTripDashboardUseCase;
        this.updateTripUseCase = updateTripUseCase;
        this.updateParticipantBudgetUseCase = updateParticipantBudgetUseCase;
        this.tripDashboardMapper = tripDashboardMapper;
        this.archiveTripUseCase = archiveTripUseCase;
        this.getTripUseCase = getTripUseCase;
        this.tripMapperDto = tripMapperDto;
        this.deleteTripUseCase = deleteTripUseCase;
        this.getTripSettlementUseCase = getTripSettlementUseCase;
        this.unarchiveTripUseCase = unarchiveTripUseCase;
    }

    @PostMapping("/{tripId}/unarchive")
    public TripDto unarchiveTrip(@AuthenticationPrincipal AuthenticatedParticipant participant,
                                 @PathVariable UUID tripId) {
        Trip trip = unarchiveTripUseCase.execute(tripId, participant.participantId());
        return tripMapperDto.toDto(trip);
    }

    @PostMapping("/{tripId}/archive")
    public TripDto archiveTrip(@AuthenticationPrincipal AuthenticatedParticipant participant,
                               @PathVariable UUID tripId) {
        Trip trip = archiveTripUseCase.execute(tripId, participant.participantId());
        return tripMapperDto.toDto(trip);
    }

    @GetMapping("/{tripId}/dashboard")
    public TripDashboardDto getTripDashboard(@AuthenticationPrincipal AuthenticatedParticipant participant,
                                             @PathVariable UUID tripId) {
        TripDashboard dashboard = getTripDashboardUseCase.execute(tripId, participant.participantId());
        return tripDashboardMapper.toDto(dashboard);
    }

    @GetMapping("/{tripId}")
    public TripDto getTrip(@AuthenticationPrincipal AuthenticatedParticipant participant,
                           @PathVariable UUID tripId) {
        Trip trip = getTripUseCase.execute(tripId, participant.participantId());
        return tripMapperDto.toDto(trip);
    }

    @PutMapping("/{tripId}")
    public TripDto updateTrip(@AuthenticationPrincipal AuthenticatedParticipant participant,
                              @PathVariable UUID tripId,
                              @Valid @RequestBody UpdateTripRequest request) {
        Trip trip = updateTripUseCase.execute(
                participant.participantId(),
                tripId,
                request.name(),
                request.startDate(),
                request.endDate(),
                request.currency());
        return tripMapperDto.toDto(trip);
    }

    @PutMapping("/{tripId}/my-budget")
    public TripDto updateMyBudget(@AuthenticationPrincipal AuthenticatedParticipant participant,
                                  @PathVariable UUID tripId,
                                  @Valid @RequestBody UpdateBudgetRequest request) {
        Trip trip = updateParticipantBudgetUseCase.execute(tripId, participant.participantId(), request.budget());
        return tripMapperDto.toDto(trip);
    }

    @GetMapping("/{tripId}/settlement")
    public List<SettlementTransferDto> getTripSettlement(@AuthenticationPrincipal AuthenticatedParticipant participant,
                                                         @PathVariable UUID tripId) {
        TripSettlement settlement = getTripSettlementUseCase.execute(tripId, participant.participantId());
        return settlement.transfers().stream()
                .map(t -> new SettlementTransferDto(
                        t.fromId(),
                        settlement.participantNames().getOrDefault(t.fromId(), "Unknown"),
                        t.toId(),
                        settlement.participantNames().getOrDefault(t.toId(), "Unknown"),
                        t.amount().amount()
                ))
                .toList();
    }

    @DeleteMapping("/{tripId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrip(@AuthenticationPrincipal AuthenticatedParticipant participant,
                           @PathVariable UUID tripId) {
        deleteTripUseCase.execute(tripId, participant.participantId());
    }
}
