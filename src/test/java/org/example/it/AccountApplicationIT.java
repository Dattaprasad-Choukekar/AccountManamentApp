package org.example.it;

import org.example.dto.AccountDto;
import org.example.dto.TransactionDto;
import org.example.dto.TransactionType;
import org.example.repository.AccountRepository;
import org.example.repository.TransactionRepository;
import org.example.service.AccountService;
import org.example.service.TransactionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class AccountApplicationIT {
    public static final String EMAIL = "test@email.com";

    @Container
    static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:latest");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    private AccountDto savedAccount;
    private TransactionDto savedTransaction;
    private static MockedStatic<UUID> UUID_MOCK;
    private static UUID uuid;

    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    @BeforeEach
    void setUp() {
        AccountDto accountDto = new AccountDto(EMAIL, List.of());
        savedAccount = accountService.createAccount(accountDto);
        savedTransaction = transactionService
                .createTransaction(new TransactionDto(savedAccount.getAccountId(), null, BigDecimal.TEN,
                        TransactionType.DEBIT));
        uuid = mockUUID();
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void createAccountTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"test1@email.com"}
                                """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId", is(uuid.toString())))
                .andExpect(jsonPath("$.email", is("test1@email.com")))
                .andExpect(jsonPath("$.transactions", empty()));
    }

    @Test
    void getAccountsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].accountId", is(savedAccount.getAccountId())))
                .andExpect(jsonPath("$[0].email", is(EMAIL)))
                .andExpect(jsonPath("$[0].transactions", hasSize(1)));
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
                .andExpect(jsonPath("$.transactions", hasSize(1)));
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
                .andExpect(jsonPath("$[0].amount", is(savedTransaction.getAmount().doubleValue())))
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
                .andExpect(jsonPath("$.amount", is(savedTransaction.getAmount().doubleValue())))
                .andExpect(jsonPath("$.type", is(savedTransaction.getType().toString())));
    }

    private static UUID mockUUID() {
        if (UUID_MOCK != null) UUID_MOCK.close();
        UUID uuid = UUID.randomUUID();
        UUID_MOCK = mockStatic(UUID.class);
        UUID_MOCK.when(UUID::randomUUID).thenReturn(uuid);
        return uuid;
    }
}
