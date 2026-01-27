package vlad.corp.money_manager_backend.infrastructure.persistence.wallet;

import org.springframework.stereotype.Repository;
import vlad.corp.money_manager_backend.domain.model.JoinCode;
import vlad.corp.money_manager_backend.domain.model.Wallet;
import vlad.corp.money_manager_backend.domain.repository.WalletRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public class WalletRepositoryImpl implements WalletRepository {

    private final WalletJpaRepository jpaRepository;

    public WalletRepositoryImpl(WalletJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }


    @Override
    public Optional<Wallet> findByJoinCode(JoinCode joinCode) {
        return jpaRepository.findByJoinCode(joinCode.value()).map(this::toDomain);
    }

    @Override
    public void save(Wallet wallet) {
        jpaRepository.save(toEntity(wallet));
    }

    @Override
    public Optional<Wallet> findById(UUID walletId) {
        Optional<WalletEntity> walletEntity = jpaRepository.findById(walletId);
        return walletEntity.map(this::toDomain);
    }

    @Override
    public List<Wallet> findByMemberId(UUID userId) {
        List<WalletEntity> wallets = jpaRepository.findAllByMemberIdsContains(userId);
        return wallets.stream().map(this::toDomain).toList();
    }

    private Wallet toDomain(WalletEntity walletEntity) {
        return Wallet.reconstitute(
                walletEntity.getWalletId(),
                walletEntity.getOwnerId(),
                walletEntity.getCreatedAt(),
                walletEntity.getMemberIds(),
                walletEntity.getName(),
                JoinCode.of(walletEntity.getJoinCode()),
                walletEntity.getCurrencyCode()

        );
    }

    private WalletEntity toEntity(Wallet wallet) {
        return WalletEntity.builder()
                .walletId(wallet.getWalletId())
                .ownerId(wallet.getOwnerId())
                .name(wallet.getName())
                .joinCode(wallet.getJoinCode().value())
                .currencyCode(wallet.getCurrencyCode())
                .createdAt(wallet.getCreatedAt())
                .memberIds(wallet.getMembers())
                .build();
    }
}
