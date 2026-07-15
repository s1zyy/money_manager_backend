package vlad.corp.money_manager_backend.presentation.dto.participant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @NotBlank @Size(min = 1, max = 100) String name
) {}
