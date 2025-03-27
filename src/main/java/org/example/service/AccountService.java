package org.example.service;

import org.example.dto.AccountDto;

import java.util.List;

public interface AccountService {
    AccountDto getAccount(String accountId);
    List<AccountDto> getAccounts();
    AccountDto createAccount(AccountDto account);
}
