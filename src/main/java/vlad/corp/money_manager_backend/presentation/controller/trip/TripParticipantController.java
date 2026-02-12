package vlad.corp.money_manager_backend.presentation.controller.trip;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vlad.corp.money_manager_backend.application.trip.ListParticipantsUseCase;
import vlad.corp.money_manager_backend.infrastructure.security.AuthenticatedParticipant;
import vlad.corp.money_manager_backend.presentation.dto.participant.ParticipantDto;
import vlad.corp.money_manager_backend.presentation.mapper.participant_mapper.ParticipantMapperDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trips")
public class TripParticipantController {
    private final ListParticipantsUseCase listParticipantsUseCase;
    private final ParticipantMapperDto participantMapperDto;

    public TripParticipantController(ListParticipantsUseCase listParticipantsUseCase, ParticipantMapperDto participantMapperDto) {
        this.listParticipantsUseCase = listParticipantsUseCase;
        this.participantMapperDto = participantMapperDto;
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
}
