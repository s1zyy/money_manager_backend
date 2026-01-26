package vlad.corp.money_manager_backend.infrastructure.persistence.wallet;

import org.springframework.stereotype.Repository;
import vlad.corp.money_manager_backend.domain.model.JoinCode;
import vlad.corp.money_manager_backend.domain.model.Wallet;
import vlad.corp.money_manager_backend.domain.repository.WalletRepository;

import java.util.Optional;
import java.util.UUID;
@Repository
public class WalletRepositoryImpl implements WalletRepository {

    private final WalletJpaRepository jpaRepository;

    public WalletRepositoryImpl(WalletJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Wallet> findByOwnerId(UUID ownerId) {
        return jpaRepository.findByOwnerId(ownerId).map(this::toDomain);
    }

    @Override
    public Optional<Wallet> findByJoinCode(JoinCode joinCode) {
        return jpaRepository.findByJoinCode(joinCode.value()).map(this::toDomain);
    }

    @Override
    public void save(Wallet wallet) {
        jpaRepository.save(toEntity(wallet));
    }

    private Wallet toDomain(WalletEntity walletEntity) {
        return Wallet.reconstitute(
                walletEntity.getWalletId(),
                walletEntity.getOwnerId(),
                walletEntity.getCreatedAt(),
                walletEntity.getMemberIds(),
                walletEntity.getName(),
                JoinCode.of(walletEntity.getJoinCode())
        );
    }

    private WalletEntity toEntity(Wallet wallet) {
        return WalletEntity.builder()
                .walletId(wallet.getWalletId())
                .ownerId(wallet.getOwnerId())
                .name(wallet.getName())
                .joinCode(wallet.getJoinCode().value())
                .createdAt(wallet.getCreatedAt())
                .memberIds(wallet.getMembers())
                .build();
    }
}
