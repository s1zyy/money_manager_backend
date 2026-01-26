package vlad.corp.money_manager_backend.domain.model;



import lombok.Getter;
import vlad.corp.money_manager_backend.domain.exception.VersionConflictException;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Transaction {

    private final UUID transactionId;
    private final UUID walletId;
    private UUID updatedBy;
    private int version;
    private Double amount;
    private String category;
    private LocalDateTime updatedAt;



    public Transaction(UUID id, Double amount, String category,
                       LocalDateTime dateTime, UUID walletId, int version, UUID updatedBy) {
        this.transactionId = id;
        this.walletId = walletId;
        this.amount = amount;
        this.category = category;
        this.updatedAt = dateTime;
        this.version = version;
        this.updatedBy = updatedBy;
    }

    public void update(
            Double newAmount,
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

        if(!this.updatedBy.equals(editorUserId) && !this.walletId.equals(editorUserId)) {
            //throw new UnauthorizedException("User cannot update this transaction");
        }
        this.amount = newAmount;
        this.category = newCategory;
        this.version++;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = editorUserId;
    }

}
