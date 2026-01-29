package vlad.corp.money_manager_backend.application.transaction;

import vlad.corp.money_manager_backend.application.exception.AccessDeniedException;
import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.exception.VersionConflictException;
import vlad.corp.money_manager_backend.domain.model.ClientAttemptSnapshot;
import vlad.corp.money_manager_backend.domain.model.Money;
import vlad.corp.money_manager_backend.domain.model.Transaction;
import vlad.corp.money_manager_backend.domain.model.Wallet;
import vlad.corp.money_manager_backend.domain.repository.TransactionRepository;
import vlad.corp.money_manager_backend.domain.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.UUID;

public class UpdateTransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    public UpdateTransactionUseCase(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    public Transaction execute(UUID userId, UUID txId, BigDecimal amount, String category, int expectedVersion){
        Transaction transaction = transactionRepository.findById(txId)
                .orElseThrow(() -> new NotFoundException("Transaction not found with this id"));
        Wallet wallet = walletRepository.findById(transaction.getWalletId())
                .orElseThrow(() -> new NotFoundException("Wallet not found with this id"));

        ClientAttemptSnapshot attempt = new ClientAttemptSnapshot(
                amount,
                category,
                expectedVersion
        );

        if(!wallet.isMember(userId)){
            throw new AccessDeniedException("User is not a member of this wallet");
        }
        try {
            transaction.update(
                    Money.of(amount, wallet.getCurrencyCode()),
                    category,
                    userId,
                    expectedVersion);
        } catch (VersionConflictException ex) {
            ex.attachClientAttemptSnapshot(attempt);
            ex.attachTransactionSnapshot(transaction.snapshot());
            throw ex;
        }
        return transactionRepository.save(transaction);
    }
}
