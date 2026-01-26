package vlad.corp.money_manager_backend.infrastructure.persistence.wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WalletJpaRepository extends JpaRepository<WalletEntity, UUID> {

    Optional<WalletEntity> findByOwnerId(UUID ownerId);
    Optional<WalletEntity> findByJoinCode(String joinCode);
}
