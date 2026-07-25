package vlad.corp.money_manager_backend.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import vlad.corp.money_manager_backend.application.port.EmailSender;
import vlad.corp.money_manager_backend.infrastructure.email.BrevoEmailSender;

@Configuration
public class EmailConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public EmailSender emailSender(
            RestTemplate restTemplate,
            @Value("${brevo.api-key}") String apiKey,
            @Value("${spring.mail.from}") String fromEmail) {
        return new BrevoEmailSender(restTemplate, apiKey, fromEmail);
    }
}
