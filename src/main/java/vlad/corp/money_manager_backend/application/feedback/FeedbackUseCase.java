package vlad.corp.money_manager_backend.application.feedback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vlad.corp.money_manager_backend.application.port.EmailSender;

import java.util.concurrent.CompletableFuture;

public class FeedbackUseCase {

    private static final Logger log = LoggerFactory.getLogger(FeedbackUseCase.class);

    private final EmailSender emailSender;
    private final String toEmail;

    public FeedbackUseCase(EmailSender emailSender, String toEmail) {
        this.emailSender = emailSender;
        this.toEmail = toEmail;
    }

    public void execute(String senderEmail, String type, String message) {
        String subject = "[TripPace " + type + "] from " + senderEmail;
        String text = "Type: " + type + "\nFrom: " + senderEmail + "\n\nMessage:\n" + message;

        CompletableFuture.runAsync(() -> {
            try {
                emailSender.send(toEmail, subject, text);
                log.info("Feedback email sent from {}", senderEmail);
            } catch (Exception e) {
                log.error("Failed to send feedback email: {}", e.getMessage());
            }
        });
    }
}
