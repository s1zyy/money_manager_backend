package vlad.corp.money_manager_backend.infrastructure.persistence.participant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface ParticipantJpaRepository extends JpaRepository<ParticipantEntity, UUID> {

    Optional<ParticipantEntity> findByEmail(String email);

    // email → null, пароль → null, ставим дату удаления
    @Modifying
    @Query("UPDATE ParticipantEntity p SET p.email = null, p.passwordHash = null, p.deletedAt = :deletedAt WHERE p.id = :id")
    void softDeleteById(@Param("id") UUID id, @Param("deletedAt") LocalDateTime deletedAt);

    @Modifying
    @Query("UPDATE ParticipantEntity p SET p.passwordHash = :newPassword WHERE p.id = :id")
    void updatePasswordById(@Param("id") UUID id, @Param("newPassword") String newPassword);
}
