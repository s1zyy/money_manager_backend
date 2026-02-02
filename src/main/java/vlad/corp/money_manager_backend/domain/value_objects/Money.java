package vlad.corp.money_manager_backend.domain.value_objects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal amount) {
    public Money {
        Objects.requireNonNull(amount, "amount must not be null");
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    public Money subtract(Money other) {
        return new Money(this.amount.subtract(other.amount));
    }
    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    public Money divide(BigDecimal divisor) {
        if(divisor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }

        return new Money(
                this.amount.divide(divisor, 2,  RoundingMode.DOWN)
        );
    }
}
