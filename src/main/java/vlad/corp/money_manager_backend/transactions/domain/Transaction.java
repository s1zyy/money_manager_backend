package vlad.corp.money_manager_backend.transactions.domain;



import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {

    private UUID id;
    private Double amount;
    private String category;
    private LocalDateTime dateTime;

    public Transaction(UUID id, Double amount, String category, LocalDateTime dateTime) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.dateTime = dateTime;
    }

    public UUID getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }
    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
