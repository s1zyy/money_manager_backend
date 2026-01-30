package vlad.corp.money_manager_backend.infrastructure.persistence.transaction;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEntity {

    @Id
    @Column(name = "transaction_id", nullable = false)
    private UUID id;

    @Column(nullable = false, name = "wallet_id")
    private UUID walletId;

    @Column(nullable = false, name = "amount")
    private BigDecimal amount;

    @Column(nullable = false, length = 3, name = "currency_code")
    private String currencyCode;

    @Column(nullable = false, name = "category")
    private String category;

    @Column(nullable = false, name = "updated_at")
    private Instant updatedAt;

    @Column(nullable = false, name = "updated_by")
    private UUID updatedBy;

    @Column(name = "version", nullable = false)
    private int version;

}
