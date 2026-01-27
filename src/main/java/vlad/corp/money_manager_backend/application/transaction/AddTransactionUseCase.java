package vlad.corp.money_manager_backend.application.transaction;

import vlad.corp.money_manager_backend.application.exception.AccessDeniedException;
import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Money;
import vlad.corp.money_manager_backend.domain.model.Transaction;
import vlad.corp.money_manager_backend.domain.model.Wallet;
import vlad.corp.money_manager_backend.domain.repository.TransactionRepository;
import vlad.corp.money_manager_backend.domain.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.UUID;

public class AddTransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    public AddTransactionUseCase(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    public Transaction execute(UUID walletId, UUID userId, BigDecimal amount, String category){
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new NotFoundException("Wallet not found with this id"));
        if(!wallet.isMember(userId)){
            throw new AccessDeniedException("User is not a member of this wallet");
        }
        Transaction transaction = Transaction.create(
                walletId,
                userId,
                Money.of(amount, wallet.getCurrencyCode()),
                category);
        return transactionRepository.save(transaction);
    }
}
