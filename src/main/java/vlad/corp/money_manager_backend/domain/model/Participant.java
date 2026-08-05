package vlad.corp.money_manager_backend.domain.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Participant {

    private final UUID id;
    private final String name;
    private final String email;
    private final String passwordHash;
    private final boolean isVirtual;
    private final String avatarUrl;

    public Participant(UUID id, String name, String email, String passwordHash, boolean isVirtual) {
        this(id, name, email, passwordHash, isVirtual, null);
    }

    public Participant(UUID id, String name, String email, String passwordHash, boolean isVirtual, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isVirtual = isVirtual;
        this.avatarUrl = avatarUrl;
    }
}
