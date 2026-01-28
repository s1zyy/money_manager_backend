package vlad.corp.money_manager_backend.presentation.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import vlad.corp.money_manager_backend.domain.exception.VersionConflictException;

import java.util.Objects;
import java.util.UUID;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VersionConflictResponse {

    @JsonProperty("transactionId")
    private final UUID transactionId;

    @JsonProperty("currentVersion")
    private final int currentVersion;

    @JsonProperty("expectedVersion")
    private final int expectedVersion;


    public VersionConflictResponse(UUID transactionId, int currentVersion, int expectedVersion) {
        this.transactionId = transactionId;
        this.currentVersion = currentVersion;
        this.expectedVersion = expectedVersion;
    }

    public static VersionConflictResponse fromException(VersionConflictException ex) {
        return new VersionConflictResponse(
                ex.getTransactionId(),
                ex.getCurrentVersion(),
                ex.getExpectedVersion()
        );
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        VersionConflictResponse that = (VersionConflictResponse) o;
        return currentVersion == that.currentVersion &&
                expectedVersion == that.expectedVersion &&
                Objects.equals(transactionId, that.transactionId);

    }
    @Override
    public int hashCode() {
        return Objects.hash(transactionId, currentVersion, expectedVersion);
    }

    @Override
    public String toString() {
        return "VersionConflictResponse{" +
                "transactionId=" + transactionId +
                ", currentVersion=" + currentVersion +
                ", expectedVersion=" + expectedVersion +
                '}';
    }
}
