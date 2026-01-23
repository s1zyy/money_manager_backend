package vlad.corp.money_manager_backend.transactions.domain;




import java.util.List;

public interface TransactionRepository{
    List<Transaction> findAll();

    void save(Transaction transaction);
}
