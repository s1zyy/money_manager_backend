package vlad.corp.money_manager_backend.domain.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Participant {

    private final UUID id;
    private final String name;
    private final String email;
    private final String passwordHash;


    public Participant(UUID id, String name, String email, String passwordHash) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }
}
