package vlad.corp.money_manager_backend.infrastructure.persistence.user;

import org.springframework.stereotype.Repository;
import vlad.corp.money_manager_backend.domain.model.User;
import vlad.corp.money_manager_backend.domain.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> findById(UUID id) {
        Optional<UserEntity> userEntity = userJpaRepository.findById(id);
        return userEntity.map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> user = userJpaRepository.findByEmail(email);
        return user.map(this::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity savedEntity = userJpaRepository.save(toEntity(user));
        return user;
    }

    private User toDomain(UserEntity entity) {
        return new User(
                entity.getUserId(),
                entity.getEmail(),
                entity.getPasswordHash()
        );
    }

    private UserEntity toEntity(User domain) {
        return UserEntity.builder()
                .userId(domain.getUserId())
                .email(domain.getEmail())
                .passwordHash(domain.getPasswordHash())
                .build();
    }

}
