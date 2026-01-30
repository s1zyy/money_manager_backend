package vlad.corp.money_manager_backend.application.auth.port;

import vlad.corp.money_manager_backend.domain.model.User;
import java.util.List;

public interface TokenGenerator {
    String generateToken(User user, List<String> roles);
}
