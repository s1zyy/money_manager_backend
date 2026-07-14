package vlad.corp.money_manager_backend.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import vlad.corp.money_manager_backend.application.feedback.FeedbackUseCase;

@Configuration
public class FeedbackUseCaseConfig {

    @Bean
    public FeedbackUseCase feedbackUseCase(
            JavaMailSender mailSender,
            @Value("${spring.mail.username}") String fromEmail) {
        return new FeedbackUseCase(mailSender, fromEmail);
    }
}
