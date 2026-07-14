package vlad.corp.money_manager_backend.presentation.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record ClaimInviteWithLoginRequest(
        @NotBlank String token,
        @NotBlank String password
) {}
