package vlad.corp.money_manager_backend.application.feedback;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class FeedbackUseCase {

    private final JavaMailSender mailSender;
    private final String fromEmail;

    public FeedbackUseCase(JavaMailSender mailSender, String fromEmail) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
    }

    public void execute(String senderEmail, String type, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(fromEmail);
        mail.setTo(fromEmail);
        mail.setSubject("[Budgi " + type + "] from " + senderEmail);
        mail.setText(
                "Type: " + type + "\n" +
                "From: " + senderEmail + "\n\n" +
                "Message:\n" + message
        );
        mailSender.send(mail);
    }
}
