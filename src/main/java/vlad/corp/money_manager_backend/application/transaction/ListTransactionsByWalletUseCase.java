package vlad.corp.money_manager_backend.application.transaction;

import vlad.corp.money_manager_backend.application.exception.AccessDeniedException;
import vlad.corp.money_manager_backend.application.exception.NotFoundException;
import vlad.corp.money_manager_backend.domain.model.Transaction;
import vlad.corp.money_manager_backend.domain.model.Wallet;
import vlad.corp.money_manager_backend.domain.repository.TransactionRepository;
import vlad.corp.money_manager_backend.domain.repository.WalletRepository;

import java.util.List;
import java.util.UUID;

public class ListTransactionsByWalletUseCase {

    private final TransactionRepository txRepository;
    private final WalletRepository walletRepository;

    public ListTransactionsByWalletUseCase(TransactionRepository txRepository, WalletRepository walletRepository) {
        this.txRepository = txRepository;
        this.walletRepository = walletRepository;
    }

    public List<Transaction> execute(UUID userId, UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new NotFoundException("Wallet not found with this id"));
        if(!wallet.isMember(userId)){
            throw new AccessDeniedException("User is not a member of this wallet");
        }

        return txRepository.findByWalletId(walletId);
    }
}
