package org.example.controller;

import org.example.dto.TransactionDto;
import org.example.dto.TransactionType;
import org.example.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {
    private static final String TRANSACTION_ID = "1234";
    private static final String ACCOUNT_ID = "1234";

    @MockitoBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getTransactionTest() throws Exception {
        TransactionDto transactionDto = new TransactionDto(ACCOUNT_ID, TRANSACTION_ID,
                BigDecimal.TEN, TransactionType.DEBIT);
        when(transactionService.getTransaction(eq(TRANSACTION_ID))).thenReturn(transactionDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/transactions/{ID}", TRANSACTION_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId", is(ACCOUNT_ID)))
                .andExpect(jsonPath("$.transactionId", is(TRANSACTION_ID)))
                .andExpect(jsonPath("$.amount", is(transactionDto.getAmount().intValue())))
                .andExpect(jsonPath("$.type", is(transactionDto.getType().toString())));
    }

    @Test
    void createAccountTest() throws Exception {
        TransactionDto transactionDto = new TransactionDto(ACCOUNT_ID, null,
                BigDecimal.TEN, TransactionType.DEBIT);
        when(transactionService.createTransaction(eq(transactionDto))).thenReturn(
                new TransactionDto(ACCOUNT_ID, TRANSACTION_ID,
                        BigDecimal.TEN, TransactionType.DEBIT));

        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                               {"accountId":"1234","amount":10,"type":"DEBIT"}
                               """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId", is(ACCOUNT_ID)))
                .andExpect(jsonPath("$.transactionId", is(TRANSACTION_ID)))
                .andExpect(jsonPath("$.amount", is(transactionDto.getAmount().intValue())))
                .andExpect(jsonPath("$.type", is(transactionDto.getType().toString())));
    }

    @Test
    void getTransactions() throws Exception {
        TransactionDto transactionDto = new TransactionDto(ACCOUNT_ID, TRANSACTION_ID, BigDecimal.TEN, TransactionType.DEBIT);
        when(transactionService.getTransactionsByAccountId(ACCOUNT_ID)).thenReturn(List.of(transactionDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/transactions")
                        .param("accountId", ACCOUNT_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].accountId", is(ACCOUNT_ID)))
                .andExpect(jsonPath("$[0].transactionId", is(TRANSACTION_ID)))
                .andExpect(jsonPath("$[0].amount", is(transactionDto.getAmount().intValue())))
                .andExpect(jsonPath("$[0].type", is(transactionDto.getType().toString())));
    }
}