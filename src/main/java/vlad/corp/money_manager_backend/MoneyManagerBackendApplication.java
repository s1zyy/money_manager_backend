package vlad.corp.money_manager_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = { UserDetailsServiceAutoConfiguration.class })
@EnableScheduling
public class  MoneyManagerBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoneyManagerBackendApplication.class, args);
    }

}
