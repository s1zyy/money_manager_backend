package vlad.corp.money_manager_backend.infrastructure.persistence.reset;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenJpaRepository extends JpaRepository<PasswordResetTokenEntity, String> {
    Optional<PasswordResetTokenEntity> findByToken(String token);
    void deleteByParticipantId(UUID participantId);
}
