package org.example.service;

import org.example.dto.AccountDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService{
    @Override
    public AccountDto getAccount(String accountId) {
        return null;
    }

    @Override
    public List<AccountDto> getAccounts() {
        return List.of();
    }

    @Override
    public AccountDto createAccount(AccountDto account) {
        return null;
    }
}
