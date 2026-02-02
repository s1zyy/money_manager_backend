package vlad.corp.money_manager_backend.infrastructure.persistence.expense;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class ExpenseEntity {

    @Id
    private UUID id;
}
