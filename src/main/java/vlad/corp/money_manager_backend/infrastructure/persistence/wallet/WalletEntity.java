package vlad.corp.money_manager_backend.infrastructure.persistence.wallet;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "wallet")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletEntity {

    @Id
    @Column(name = "wallet_id")
    private UUID walletId;

    @Column(nullable = false)
    private UUID ownerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 8)
    private String joinCode;

    @Column(nullable = false, length = 3)
    private String currencyCode;


    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "wallet_member",
            joinColumns = @JoinColumn(name = "wallet_id", referencedColumnName = "wallet_id")
    )
    @Column(name = "member_id", nullable = false)
    @Builder.Default
    private Set<UUID> memberIds = new HashSet<>();

}
