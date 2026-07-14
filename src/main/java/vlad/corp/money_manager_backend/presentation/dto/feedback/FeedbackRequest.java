package vlad.corp.money_manager_backend.presentation.dto.feedback;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FeedbackRequest(
        @NotBlank String type,
        @NotBlank @Size(max = 2000) String message
) {}
