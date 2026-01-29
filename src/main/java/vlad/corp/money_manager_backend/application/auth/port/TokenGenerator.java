package vlad.corp.money_manager_backend.application.auth.port;

import vlad.corp.money_manager_backend.domain.model.User;

public interface TokenGenerator {
    String generateToken(User user);
}
