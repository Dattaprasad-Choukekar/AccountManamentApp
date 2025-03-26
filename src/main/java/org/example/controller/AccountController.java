package org.example.controller;

import org.example.model.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/accounts/")
public class AccountController {

    @GetMapping("${id}")
    public Account getAccount(@PathVariable int id) {
        return null;
    }

    @GetMapping()
    public List<Account> getAccounts() {
        return null;
    }

    @PostMapping()
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return null;
    }
}
