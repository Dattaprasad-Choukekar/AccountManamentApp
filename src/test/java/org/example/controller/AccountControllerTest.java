package org.example.controller;

import org.example.dto.AccountDto;
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

    @MockitoBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAccountTest() throws Exception {
        String id = "1234";
        AccountDto account = new AccountDto(id, List.of());
        when(accountService.getAccount(eq(id))).thenReturn(account);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{id}", id))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId", is(id)))
                .andExpect(jsonPath("$.transactions", empty()));
    }

    @Test
    void getAccountsTest() throws Exception {
        String id = "1234";
        when(accountService.getAccounts()).thenReturn(List.of(new AccountDto(id, List.of())));

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].accountId", is(id)))
                .andExpect(jsonPath("$[0].transactions", empty()));
    }

    @Test
    void createAccountTest() throws Exception {
        String id = "1234";
        AccountDto account = new AccountDto(null, List.of());
        when(accountService.createAccount(eq(account))).thenReturn(new AccountDto(id, List.of()));

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"accountId":null,"transactions":[]}
                                """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId", is(id)))
                .andExpect(jsonPath("$.transactions", empty()));
    }
}