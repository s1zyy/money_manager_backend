package vlad.corp.money_manager_backend.domain.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class User {

    private final UUID userId;

    public User(UUID userId) {
        this.userId = userId;
    }
}
