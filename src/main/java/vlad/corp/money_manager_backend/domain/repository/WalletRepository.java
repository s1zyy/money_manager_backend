package vlad.corp.money_manager_backend.domain.repository;

import vlad.corp.money_manager_backend.domain.model.JoinCode;
import vlad.corp.money_manager_backend.domain.model.Wallet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository {


    Optional<Wallet> findByJoinCode(JoinCode joinCode);

    void save(Wallet wallet);

    Optional<Wallet> findById(UUID walletId);
    List<Wallet> findByMemberId(UUID userId);
}
