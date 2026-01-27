package vlad.corp.money_manager_backend.infrastructure.persistence.transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findAllByWalletId(UUID walletId);
}
