package vlad.corp.money_manager_backend.application.wallet;

import vlad.corp.money_manager_backend.domain.model.JoinCode;

public interface JoinCodeGenerator {
    JoinCode generate();
}
