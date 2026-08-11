package vlad.corp.money_manager_backend.application.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vlad.corp.money_manager_backend.application.port.EmailSender;
import vlad.corp.money_manager_backend.domain.model.PasswordResetToken;
import vlad.corp.money_manager_backend.domain.repository.ParticipantRepository;
import vlad.corp.money_manager_backend.domain.repository.PasswordResetTokenRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public class ForgotPasswordUseCase {

    private static final Logger log = LoggerFactory.getLogger(ForgotPasswordUseCase.class);
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TOKEN_LENGTH = 8;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final ParticipantRepository participantRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailSender emailSender;

    public ForgotPasswordUseCase(ParticipantRepository participantRepository,
                                 PasswordResetTokenRepository tokenRepository,
                                 EmailSender emailSender) {
        this.participantRepository = participantRepository;
        this.tokenRepository = tokenRepository;
        this.emailSender = emailSender;
    }

    public void execute(String email) {
        participantRepository.findByEmail(email).ifPresent(participant -> {
            tokenRepository.deleteByParticipantId(participant.getId());

            String token = generateToken();
            tokenRepository.save(new PasswordResetToken(token, participant.getId(), LocalDateTime.now().plusHours(1)));

            String name = participant.getName();
            CompletableFuture.runAsync(() -> {
                try {
                    sendResetEmail(email, name, token);
                    log.info("Password reset email sent to {}", email);
                } catch (Exception e) {
                    log.error("Failed to send reset email to {}: {}", email, e.getMessage());
                }
            });
        });
    }

    private String generateToken() {
        StringBuilder sb = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    private void sendResetEmail(String toEmail, String name, String token) {
        String deepLink = "https://trippace.app/reset?token=" + token;
        String subject = "Reset your TripPace password";
        String html = "<p>Hi, <b>" + name + "</b>!</p>" +
                "<p>We received a request to reset your password.</p>" +
                "<p>Tap the button below to reset it:</p>" +
                "<p><a href=\"" + deepLink + "\" style=\"background:#6C63FF;color:white;padding:12px 24px;border-radius:8px;text-decoration:none;font-weight:bold;\">Reset Password</a></p>" +
                "<p>Or enter this code manually in the app:</p>" +
                "<p style=\"font-size:24px;font-weight:bold;letter-spacing:6px;\">" + token + "</p>" +
                "<p style=\"color:#888;\">The link is valid for 1 hour. If you didn't request this, ignore this email.</p>";
        emailSender.send(toEmail, subject, html);
    }
}
