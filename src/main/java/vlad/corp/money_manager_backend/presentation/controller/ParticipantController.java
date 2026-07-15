package vlad.corp.money_manager_backend.presentation.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vlad.corp.money_manager_backend.application.participant.ChangePasswordUseCase;
import vlad.corp.money_manager_backend.application.participant.UpdateProfileUseCase;
import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Participant;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;
import vlad.corp.money_manager_backend.infrastructure.security.AuthenticatedParticipant;
import vlad.corp.money_manager_backend.presentation.dto.participant.ChangePasswordRequest;
import vlad.corp.money_manager_backend.presentation.dto.participant.ParticipantProfileResponse;
import vlad.corp.money_manager_backend.presentation.dto.participant.UpdateProfileRequest;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    private final ParticipantRepository participantRepository;
    private final UpdateProfileUseCase updateProfileUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;

    public ParticipantController(ParticipantRepository participantRepository,
                                 UpdateProfileUseCase updateProfileUseCase,
                                 ChangePasswordUseCase changePasswordUseCase) {
        this.participantRepository = participantRepository;
        this.updateProfileUseCase = updateProfileUseCase;
        this.changePasswordUseCase = changePasswordUseCase;
    }

    @GetMapping("/me")
    public ParticipantProfileResponse getMe(@AuthenticationPrincipal AuthenticatedParticipant auth) {
        Participant p = participantRepository.findById(auth.participantId())
                .orElseThrow(() -> new NotFoundException("Participant not found"));
        return new ParticipantProfileResponse(p.getName(), p.getEmail());
    }

    @PatchMapping("/me")
    public ParticipantProfileResponse updateProfile(
            @AuthenticationPrincipal AuthenticatedParticipant auth,
            @Valid @RequestBody UpdateProfileRequest request) {
        Participant updated = updateProfileUseCase.execute(auth.participantId(), request.name());
        return new ParticipantProfileResponse(updated.getName(), updated.getEmail());
    }

    @PatchMapping("/me/password")
    public void changePassword(
            @AuthenticationPrincipal AuthenticatedParticipant auth,
            @Valid @RequestBody ChangePasswordRequest request) {
        changePasswordUseCase.execute(auth.participantId(), request.currentPassword(), request.newPassword());
    }
}
