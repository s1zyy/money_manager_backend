package vlad.corp.money_manager_backend.application.feedback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.concurrent.CompletableFuture;

public class FeedbackUseCase {

    private static final Logger log = LoggerFactory.getLogger(FeedbackUseCase.class);

    private final JavaMailSender mailSender;
    private final String fromEmail;
    private final String toEmail;

    public FeedbackUseCase(JavaMailSender mailSender, String fromEmail, String toEmail) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
    }

    public void execute(String senderEmail, String type, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(fromEmail);
        mail.setTo(toEmail);
        mail.setSubject("[TripPace " + type + "] from " + senderEmail);
        mail.setText(
                "Type: " + type + "\n" +
                "From: " + senderEmail + "\n\n" +
                "Message:\n" + message
        );
        CompletableFuture.runAsync(() -> {
            try {
                mailSender.send(mail);
                log.info("Feedback email sent from {}", senderEmail);
            } catch (Exception e) {
                log.error("Failed to send feedback email: {}", e.getMessage());
            }
        });
    }
}
