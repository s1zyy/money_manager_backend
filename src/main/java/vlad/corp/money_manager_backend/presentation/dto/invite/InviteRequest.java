package vlad.corp.money_manager_backend.presentation.dto.invite;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record InviteRequest(@Email @NotBlank String email) {}
