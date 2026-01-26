package vlad.corp.money_manager_backend.domain.repository;

import vlad.corp.money_manager_backend.domain.model.Transaction;

import java.util.List;

public interface TransactionRepository{

    List<Transaction> findAll();

    void save(Transaction transaction);
}
