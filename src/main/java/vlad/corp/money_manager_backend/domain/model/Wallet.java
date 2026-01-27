package vlad.corp.money_manager_backend.domain.model;

import lombok.AccessLevel;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Getter
public class Wallet {
    private final UUID walletId;
    private final UUID ownerId;
    @Getter(AccessLevel.NONE)
    private final Set<UUID> memberIds;
    private final LocalDateTime createdAt;
    private final String name;
    private final JoinCode joinCode;
    private final String currencyCode;

    private Wallet(UUID walletId, UUID ownerId, LocalDateTime createdAt,
                   Set<UUID> memberIds, String name, JoinCode joinCode, String currencyCode) {
        this.walletId = walletId;
        this.ownerId = ownerId;
        this.memberIds = new HashSet<>(memberIds);
        this.createdAt = createdAt;
        this.name = name;
        this.joinCode = joinCode;
        this.currencyCode = currencyCode;
    }

    public static Wallet create(UUID ownerId, String name, JoinCode joinCode, String currencyCode) {
        Set<UUID> members = new HashSet<>();
        members.add(ownerId);
        return new Wallet(
                UUID.randomUUID(),
                ownerId,
                LocalDateTime.now(),
                members,
                name,
                joinCode,
                currencyCode);
    }

    public static Wallet reconstitute(UUID walletId, UUID ownerId, LocalDateTime createdAt,
                                      Set<UUID> memberIds, String name, JoinCode joinCode,
                                      String currencyCode
    ) {
        return new Wallet(
                walletId,
                ownerId,
                createdAt,
                memberIds,
                name,
                joinCode,
                currencyCode);
    }

    public void join(UUID userId) {
        memberIds.add(userId);
    }

    public void inviteMember(UUID actorId, UUID newMemberId) {}

    public void removeMember(UUID actorId, UUID memberId) {}

    public void changeOwner(UUID actorId, UUID newOwnerId) {}

    public boolean isMember(UUID userId) {
        return memberIds.contains(userId);
    }

    public Set<UUID> getMembers() {
        return Set.copyOf(memberIds);
    }
}
