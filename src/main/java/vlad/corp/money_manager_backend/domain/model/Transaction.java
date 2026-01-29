package vlad.corp.money_manager_backend.domain.model;

import lombok.Getter;
import vlad.corp.money_manager_backend.domain.exception.VersionConflictException;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Transaction {

    private final UUID transactionId;
    private final UUID walletId;
    private UUID updatedBy;
    private int version;
    private Money amount;
    private String category;
    private Instant updatedAt;



    public Transaction(UUID id, UUID walletId, Money amount,
                       String category, Instant updatedAt, int version, UUID updatedBy){
        this.transactionId = id;
        this.walletId = walletId;
        this.amount = amount;
        this.category = category;
        this.updatedAt = updatedAt;
        this.version = version;
        this.updatedBy = updatedBy;
    }

    public static Transaction create(
            UUID walletId, UUID createdBy, Money amount, String category
            ){
        return new Transaction(
                UUID.randomUUID(),
                walletId,
                amount,
                category,
                Instant.now(),
                1,
                createdBy
        );
    }

    public void update(
            Money newAmount,
            String newCategory,
            UUID editorUserId,
            int expectedVersion
    ){
        if(this.version != expectedVersion) {
            throw new VersionConflictException(
                    this.transactionId,
                    this.version,
                    expectedVersion
            );
        }

        this.amount = newAmount;
        this.category = newCategory;
        this.version++;
        this.updatedAt = Instant.now();
        this.updatedBy = editorUserId;
    }

    public TransactionSnapshot snapshot(){
        return new TransactionSnapshot(
                this.transactionId,
                this.walletId,
                this.updatedBy,
                this.version,
                this.amount.amount(),
                this.amount.currency().getCurrencyCode(),
                this.category,
                this.updatedAt
        );
    }

}
