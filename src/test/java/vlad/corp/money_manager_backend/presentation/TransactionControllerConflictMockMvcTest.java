package vlad.corp.money_manager_backend.presentation;


import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import vlad.corp.money_manager_backend.application.transaction.AddTransactionUseCase;
import vlad.corp.money_manager_backend.application.transaction.ListTransactionsByWalletUseCase;
import vlad.corp.money_manager_backend.application.transaction.UpdateTransactionUseCase;
import vlad.corp.money_manager_backend.domain.exception.VersionConflictException;
import vlad.corp.money_manager_backend.presentation.controller.TransactionController;
import vlad.corp.money_manager_backend.presentation.dto.transaction.UpdateTransactionRequest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;





@WebMvcTest(TransactionController.class)
@Import(ApiExceptionHandler.class)
public class TransactionControllerConflictMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UpdateTransactionUseCase updateTransactionUseCase;

    @MockitoBean
    private AddTransactionUseCase addTransactionUseCase;

    @MockitoBean
    private ListTransactionsByWalletUseCase listTransactionsByWalletUseCase;

    @Test
    void whenUpdateThrowsVersionConflict_thenReturns409AndBody() throws Exception {
        UUID transactionId = UUID.randomUUID();
        int currentVersion = 3;
        int expectedVersion = 2;

        VersionConflictException ex =
                new VersionConflictException(transactionId, currentVersion, expectedVersion);

        doThrow(ex).when(updateTransactionUseCase).execute(any(UUID.class), any(UUID.class), any(BigDecimal.class), any(String.class), anyInt());

        UpdateTransactionRequest rq = new UpdateTransactionRequest(
                new BigDecimal("100.00"),
                "Food",
                expectedVersion
        );

        String requestJson = objectMapper.writeValueAsString(rq);


        mockMvc.perform(put("/api/transactions/{transactionId}", transactionId)
                        .header("X-User-Id", UUID.randomUUID().toString())
                    .content(requestJson)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionId").value(transactionId.toString()))
                .andExpect(jsonPath("$.currentVersion").value(currentVersion))
                .andExpect(jsonPath("$.expectedVersion").value(expectedVersion));


    }

}
