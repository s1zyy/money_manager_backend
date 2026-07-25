package vlad.corp.money_manager_backend.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vlad.corp.money_manager_backend.application.feedback.FeedbackUseCase;
import vlad.corp.money_manager_backend.application.port.EmailSender;

@Configuration
public class FeedbackUseCaseConfig {

    @Bean
    public FeedbackUseCase feedbackUseCase(
            EmailSender emailSender,
            @Value("${feedback.to-email}") String toEmail) {
        return new FeedbackUseCase(emailSender, toEmail);
    }
}
