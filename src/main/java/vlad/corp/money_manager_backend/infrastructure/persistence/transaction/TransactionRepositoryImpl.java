package vlad.corp.money_manager_backend.infrastructure.persistence.transaction;

import org.springframework.stereotype.Repository;
import vlad.corp.money_manager_backend.domain.model.Money;
import vlad.corp.money_manager_backend.domain.model.Transaction;
import vlad.corp.money_manager_backend.domain.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionJpaRepository jpaRepository;

    public TransactionRepositoryImpl(TransactionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return toDomain(jpaRepository.save(toEntity(transaction)));
    }

    @Override
    public Optional<Transaction> findById(UUID transactionId) {
        return jpaRepository.findById(transactionId).map(this::toDomain);
    }

    @Override
    public List<Transaction> findByWalletId(UUID walletId) {
        return jpaRepository.findAllByWalletId(walletId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private Transaction toDomain(TransactionEntity entity) {
        return new Transaction(
                entity.getId(),
                entity.getWalletId(),
                Money.of(entity.getAmount(), entity.getCurrencyCode()),
                entity.getCategory(),
                entity.getUpdatedAt(),
                entity.getVersion(),
                entity.getUpdatedBy()
        );
    }

    private TransactionEntity toEntity(Transaction transaction) {
        return TransactionEntity.builder()
                .id(transaction.getTransactionId())
                .walletId(transaction.getWalletId())
                .amount(transaction.getAmount().amount())
                .currencyCode(transaction.getAmount().currency().getCurrencyCode())
                .category(transaction.getCategory())
                .updatedAt(transaction.getUpdatedAt())
                .updatedBy(transaction.getUpdatedBy())
                .version(transaction.getVersion())
                .build();
    }
}
