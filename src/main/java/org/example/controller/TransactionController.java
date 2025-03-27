package org.example.controller;

import org.example.model.Transaction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/transactions/")
public class TransactionController {

    @GetMapping("{id}")
    public Transaction getTransaction(@PathVariable int id) {
        return null;
    }

   /* @GetMapping("{accountId}")
    public List<Transaction> getTransactions(@PathVariable int accountId) {
        return null;
    }*/
}
