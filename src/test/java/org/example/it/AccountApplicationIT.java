package org.example.it;

import org.example.dto.AccountDto;
import org.example.dto.TransactionDto;
import org.example.dto.TransactionType;
import org.example.repository.AccountRepository;
import org.example.repository.TransactionRepository;
import org.example.service.AccountService;
import org.example.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AccountApplicationIT {
    public static final String EMAIL = "test@email.com";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

   /* @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;
*/
    private AccountDto savedAccount;
    private TransactionDto savedTransaction;
    private static UUID uuid = mockUUID();

    @BeforeEach
    void setUp() {
        AccountDto accountDto = new AccountDto(EMAIL, List.of());
        savedAccount = accountService.createAccount(accountDto);
        savedTransaction = transactionService
                .createTransaction(new TransactionDto(savedAccount.getAccountId(), null, BigDecimal.TEN,
                        TransactionType.DEBIT));
    }

    @Test
    void createAccountTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"test@email.com","transactions":[]}
                                """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId", is(uuid.toString())))
                .andExpect(jsonPath("$.email", is(EMAIL)))
                .andExpect(jsonPath("$.transactions", empty()));
    }

    @Test
    void getAccountsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].accountId", is(savedAccount.getAccountId())))
                .andExpect(jsonPath("$[0].email", is(EMAIL)))
                .andExpect(jsonPath("$[0].transactions", empty()));
    }

    @Test
    void getAccountTest() throws Exception {
        String accountId = savedAccount.getAccountId();
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{id}", accountId))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId", is(accountId)))
                .andExpect(jsonPath("$.email", is(EMAIL)))
                .andExpect(jsonPath("$.transactions", empty()));
    }

    @Test
    void createTransactionTest() throws Exception {

        String accountId = savedAccount.getAccountId();
        String requestBody = """
                {"accountId":"%s","amount":10,"type":"DEBIT"}
                """;
        requestBody = String.format(requestBody, accountId);
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId", is(accountId)))
                .andExpect(jsonPath("$.transactionId", is(uuid.toString())))
                .andExpect(jsonPath("$.amount", is(10)))
                .andExpect(jsonPath("$.type", is("DEBIT")));
    }

    @Test
    void getTransactionsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/transactions")
                        .param("accountId", savedAccount.getAccountId()))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].accountId", is(savedAccount.getAccountId())))
                .andExpect(jsonPath("$[0].transactionId", is(savedTransaction.getTransactionId())))
                .andExpect(jsonPath("$[0].amount", is(savedTransaction.getAmount().intValue())))
                .andExpect(jsonPath("$[0].type", is(savedTransaction.getType().toString())));
    }

    @Test
    void getTransactionTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/transactions/{id}", savedTransaction.getTransactionId()))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId", is(savedTransaction.getAccountId())))
                .andExpect(jsonPath("$.transactionId", is(savedTransaction.getTransactionId())))
                .andExpect(jsonPath("$.amount", is(savedTransaction.getAmount().intValue())))
                .andExpect(jsonPath("$.type", is(savedTransaction.getType().toString())));
    }

    private static UUID mockUUID() {
        UUID uuid = UUID.randomUUID();
        MockedStatic<UUID> mocked = mockStatic(UUID.class);
        mocked.when(UUID::randomUUID).thenReturn(uuid);
        return uuid;
    }
}
