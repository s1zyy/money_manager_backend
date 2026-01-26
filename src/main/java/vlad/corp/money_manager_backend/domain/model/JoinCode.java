package vlad.corp.money_manager_backend.domain.model;

import java.util.Objects;

public record JoinCode(String value) {

    public JoinCode {
        Objects.requireNonNull(value, "joinCode must not be null");

        if(!value.matches("\\d{8}")) {
            throw new IllegalArgumentException("Join code must be 8 digits long");
        }
    }

    public static JoinCode of(String value) {
        Objects.requireNonNull(value, "joinCode must not be null");
        return new JoinCode(value.trim());
    }


}
