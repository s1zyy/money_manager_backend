package vlad.corp.money_manager_backend.presentation.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vlad.corp.money_manager_backend.application.feedback.FeedbackUseCase;
import vlad.corp.money_manager_backend.infrastructure.security.AuthenticatedParticipant;
import vlad.corp.money_manager_backend.presentation.dto.feedback.FeedbackRequest;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackUseCase feedbackUseCase;

    public FeedbackController(FeedbackUseCase feedbackUseCase) {
        this.feedbackUseCase = feedbackUseCase;
    }

    @PostMapping
    public ResponseEntity<?> sendFeedback(
            @AuthenticationPrincipal AuthenticatedParticipant principal,
            @Valid @RequestBody FeedbackRequest request) {
        feedbackUseCase.execute(principal.email(), request.type(), request.message());
        return ResponseEntity.ok().build();
    }
}
