package vlad.corp.money_manager_backend.transactions.application;


import org.springframework.stereotype.Service;
import vlad.corp.money_manager_backend.transactions.domain.Transaction;
import vlad.corp.money_manager_backend.transactions.domain.TransactionRepository;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public void createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
