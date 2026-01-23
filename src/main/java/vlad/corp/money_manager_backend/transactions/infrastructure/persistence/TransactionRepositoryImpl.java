package vlad.corp.money_manager_backend.transactions.infrastructure.persistence;

import org.springframework.stereotype.Repository;
import vlad.corp.money_manager_backend.transactions.domain.Transaction;
import vlad.corp.money_manager_backend.transactions.domain.TransactionRepository;

import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionJpaRepository jpaRepository;

    public TransactionRepositoryImpl(TransactionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Transaction> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }



    @Override
    public void save(Transaction transaction) {
        jpaRepository.save(toEntity(transaction));
    }

    private Transaction toDomain(TransactionEntity entity) {
        return new Transaction(
                entity.getId(),
                entity.getAmount(),
                entity.getCategory(),
                entity.getDateTime()
        );
    }

    private TransactionEntity toEntity(Transaction transaction){
        return TransactionEntity.builder()
                .amount(transaction.getAmount())
                .category(transaction.getCategory())
                .dateTime(transaction.getDateTime())
                .build();
    }
}
