package vlad.corp.money_manager_backend.application.auth.port;

import vlad.corp.money_manager_backend.domain.model.Participant;

import java.util.List;

public interface TokenGenerator {
    String generateToken(Participant participant, List<String> roles);
}
