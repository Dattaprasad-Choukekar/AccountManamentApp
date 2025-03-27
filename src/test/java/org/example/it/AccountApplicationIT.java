package org.example.it;

import org.example.dto.AccountDto;
import org.example.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountApplicationIT {

    public static final String EMAIL = "test@email.com";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;
    private AccountDto savedAccount;

    @BeforeEach
    void setUp() {
        AccountDto accountDto = new AccountDto(EMAIL, List.of());
        savedAccount = accountService.createAccount(accountDto);
    }

    @Test
    void createAccountTest() throws Exception {
        UUID uuid = mockUUID();
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

    private static UUID mockUUID() {
        UUID uuid = UUID.randomUUID();
        MockedStatic<UUID> mocked = mockStatic(UUID.class);
        mocked.when(UUID::randomUUID).thenReturn(uuid);
        return uuid;
    }
}
