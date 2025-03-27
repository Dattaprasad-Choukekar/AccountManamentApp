package org.example.controller;

import org.example.dto.AccountDto;
import org.example.exception.AccountNotFoundException;
import org.example.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {
    private static final String EMAIL = "test@email.com";
    private static final String ID = "1234";

    @MockitoBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAccountTest() throws Exception {
        AccountDto account = new AccountDto(ID, EMAIL, List.of());
        when(accountService.getAccount(eq(ID))).thenReturn(account);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{ID}", ID))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId", is(ID)))
                .andExpect(jsonPath("$.email", is(EMAIL)))
                .andExpect(jsonPath("$.transactions", empty()));
    }

    @Test
    void getAccountsTest() throws Exception {
        when(accountService.getAccounts()).thenReturn(List.of(new AccountDto(ID,
                EMAIL, List.of())));

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].accountId", is(ID)))
                .andExpect(jsonPath("$[0].email", is(EMAIL)))
                .andExpect(jsonPath("$[0].transactions", empty()));
    }

    @Test
    void createAccountTest() throws Exception {
        AccountDto account = new AccountDto(EMAIL, List.of());
        when(accountService.createAccount(eq(account))).thenReturn(new AccountDto(ID, EMAIL,
                List.of()));

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"test@email.com","transactions":[]}
                                """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId", is(ID)))
                .andExpect(jsonPath("$.email", is(EMAIL)))
                .andExpect(jsonPath("$.transactions", empty()));
    }

    @Test
    void createAccountInvalidEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"abc","transactions":[]}
                                """))
               // .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void accountNotFoundExceptionTest() throws Exception {
        String accountIdNotFound = "999";
        when(accountService.getAccount(eq(accountIdNotFound))).thenThrow(new AccountNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{ID}", accountIdNotFound))
                //.andDo(print())
                .andExpect(status().isNotFound());
    }
}