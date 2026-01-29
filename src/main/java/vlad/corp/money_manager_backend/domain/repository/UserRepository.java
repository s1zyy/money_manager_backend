package vlad.corp.money_manager_backend.domain.repository;

import vlad.corp.money_manager_backend.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    User save(User user);
}
