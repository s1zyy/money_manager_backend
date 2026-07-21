package vlad.corp.money_manager_backend.presentation.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vlad.corp.money_manager_backend.application.participant.ChangePasswordUseCase;
import vlad.corp.money_manager_backend.application.participant.DeleteAccountUseCase;
import vlad.corp.money_manager_backend.application.participant.FullDeleteAccountUseCase;
import vlad.corp.money_manager_backend.application.participant.UpdateProfileUseCase;
import vlad.corp.money_manager_backend.application.exceptions.NotFoundException;
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
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final FullDeleteAccountUseCase fullDeleteAccountUseCase;

    public ParticipantController(ParticipantRepository participantRepository,
                                 UpdateProfileUseCase updateProfileUseCase,
                                 ChangePasswordUseCase changePasswordUseCase,
                                 DeleteAccountUseCase deleteAccountUseCase,
                                 FullDeleteAccountUseCase fullDeleteAccountUseCase) {
        this.participantRepository = participantRepository;
        this.updateProfileUseCase = updateProfileUseCase;
        this.changePasswordUseCase = changePasswordUseCase;
        this.deleteAccountUseCase = deleteAccountUseCase;
        this.fullDeleteAccountUseCase = fullDeleteAccountUseCase;
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

    @DeleteMapping("/me")
    public void deleteAccount(@AuthenticationPrincipal AuthenticatedParticipant auth) {
        deleteAccountUseCase.execute(auth.participantId());
    }

    @DeleteMapping("/me/full")
    public void fullDeleteAccount(@AuthenticationPrincipal AuthenticatedParticipant auth) {
        fullDeleteAccountUseCase.execute(auth.participantId());
    }
}
