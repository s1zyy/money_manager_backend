package vlad.corp.money_manager_backend.presentation.controller.trip;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vlad.corp.money_manager_backend.application.invite.InviteVirtualParticipantUseCase;
import vlad.corp.money_manager_backend.application.trip.AddVirtualParticipantUseCase;
import vlad.corp.money_manager_backend.application.trip.LeaveTripUseCase;
import vlad.corp.money_manager_backend.application.trip.ListParticipantsUseCase;
import vlad.corp.money_manager_backend.application.trip.RemoveParticipantUseCase;
import vlad.corp.money_manager_backend.application.participant.UpdateAnyParticipantBudgetUseCase;
import vlad.corp.money_manager_backend.presentation.dto.invite.InviteRequest;
import vlad.corp.money_manager_backend.presentation.dto.trip.UpdateBudgetRequest;
import vlad.corp.money_manager_backend.domain.model.Participant;
import vlad.corp.money_manager_backend.infrastructure.security.AuthenticatedParticipant;
import vlad.corp.money_manager_backend.presentation.dto.participant.AddVirtualParticipantRequest;
import vlad.corp.money_manager_backend.presentation.dto.participant.ParticipantDto;
import vlad.corp.money_manager_backend.presentation.mapper.participant_mapper.ParticipantMapperDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trips")
public class TripParticipantController {
    private final ListParticipantsUseCase listParticipantsUseCase;
    private final LeaveTripUseCase leaveTripUseCase;
    private final RemoveParticipantUseCase removeParticipantUseCase;
    private final ParticipantMapperDto participantMapperDto;
    private final AddVirtualParticipantUseCase addVirtualParticipantUseCase;
    private final UpdateAnyParticipantBudgetUseCase updateAnyParticipantBudgetUseCase;
    private final InviteVirtualParticipantUseCase inviteVirtualParticipantUseCase;

    public TripParticipantController(ListParticipantsUseCase listParticipantsUseCase,
                                     LeaveTripUseCase leaveTripUseCase,
                                     RemoveParticipantUseCase removeParticipantUseCase,
                                     ParticipantMapperDto participantMapperDto,
                                     AddVirtualParticipantUseCase addVirtualParticipantUseCase,
                                     UpdateAnyParticipantBudgetUseCase updateAnyParticipantBudgetUseCase,
                                     InviteVirtualParticipantUseCase inviteVirtualParticipantUseCase) {
        this.listParticipantsUseCase = listParticipantsUseCase;
        this.leaveTripUseCase = leaveTripUseCase;
        this.removeParticipantUseCase = removeParticipantUseCase;
        this.participantMapperDto = participantMapperDto;
        this.addVirtualParticipantUseCase = addVirtualParticipantUseCase;
        this.updateAnyParticipantBudgetUseCase = updateAnyParticipantBudgetUseCase;
        this.inviteVirtualParticipantUseCase = inviteVirtualParticipantUseCase;
    }

    @GetMapping("/{tripId}/participants")
    public List<ParticipantDto> listParticipants(
            @AuthenticationPrincipal AuthenticatedParticipant participant,
            @PathVariable UUID tripId
    ) {
        return listParticipantsUseCase.execute(tripId, participant.participantId())
                .stream()
                .map(participantMapperDto::toDto)
                .toList();
    }

    @PostMapping("/{tripId}/leave")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void leaveTrip(
            @AuthenticationPrincipal AuthenticatedParticipant participant,
            @PathVariable UUID tripId
    ) {
        leaveTripUseCase.execute(tripId, participant.participantId());
    }

    @DeleteMapping("/{tripId}/participants/{participantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeParticipant(
            @AuthenticationPrincipal AuthenticatedParticipant participant,
            @PathVariable(name = "tripId") UUID tripId,
            @PathVariable(name = "participantId") UUID toRemove
    ) {
        removeParticipantUseCase.execute(tripId, participant.participantId(), toRemove);
    }

    @PutMapping("/{tripId}/participants/{participantId}/budget")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateParticipantBudget(
            @AuthenticationPrincipal AuthenticatedParticipant owner,
            @PathVariable UUID tripId,
            @PathVariable UUID participantId,
            @RequestBody UpdateBudgetRequest request
    ) {
        updateAnyParticipantBudgetUseCase.execute(tripId, owner.participantId(), participantId, request.budget());
    }

    @PostMapping("/{tripId}/participants/virtual")
    public ParticipantDto addVirtualParticipant(
            @AuthenticationPrincipal AuthenticatedParticipant participant,
            @PathVariable UUID tripId,
            @RequestBody AddVirtualParticipantRequest request
    ) {
        Participant virtual = addVirtualParticipantUseCase.execute(tripId, participant.participantId(), request.name(), request.budget());
        return participantMapperDto.toDto(virtual);
    }

    @PostMapping("/{tripId}/participants/{participantId}/invite")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inviteVirtualParticipant(
            @AuthenticationPrincipal AuthenticatedParticipant owner,
            @PathVariable UUID tripId,
            @PathVariable UUID participantId,
            @Valid @RequestBody InviteRequest request
    ) {
        inviteVirtualParticipantUseCase.execute(tripId, owner.participantId(), participantId, request.email());
    }
}
