package vlad.corp.money_manager_backend.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class CommonConfig {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
