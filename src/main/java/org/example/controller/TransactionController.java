package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.TransactionDto;
import org.example.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/{transactionId}")
    public TransactionDto getTransaction(@PathVariable String transactionId) {
        return transactionService.getTransaction(transactionId);
    }

    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(@Valid @RequestBody TransactionDto transactionDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.createTransaction(transactionDto));
    }

    @GetMapping()
    public List<TransactionDto> getTransactions(@RequestParam String accountId) {
        return transactionService.getTransactionsByAccountId(accountId);
    }
}
