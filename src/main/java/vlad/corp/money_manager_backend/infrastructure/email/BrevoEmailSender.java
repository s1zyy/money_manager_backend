package vlad.corp.money_manager_backend.infrastructure.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import vlad.corp.money_manager_backend.application.port.EmailSender;

import java.util.List;
import java.util.Map;

public class BrevoEmailSender implements EmailSender {

    private static final Logger log = LoggerFactory.getLogger(BrevoEmailSender.class);
    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String fromEmail;

    public BrevoEmailSender(RestTemplate restTemplate, String apiKey, String fromEmail) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.fromEmail = fromEmail;
    }

    @Override
    public void send(String to, String subject, String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "sender", Map.of("name", "TripPace", "email", fromEmail),
                "to", List.of(Map.of("email", to)),
                "subject", subject,
                "htmlContent", text,
                "trackClicks", false
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(BREVO_API_URL, request, String.class);
        log.info("Email sent via Brevo API to {}", to);
    }
}
