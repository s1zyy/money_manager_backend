package vlad.corp.money_manager_backend.domain.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class User {

    private final UUID userId;
    private final String email;
    private final String passwordHash;


    public User(UUID userId, String email, String passwordHash) {
        this.userId = userId;
        this.email = email;
        this.passwordHash = passwordHash;
    }
}
