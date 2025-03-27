package org.example.service;

import org.example.dto.AccountDto;
import org.example.exception.AccountNotFoundException;
import org.example.model.Account;
import org.example.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDto getAccount(String accountId) {
        Optional<Account> accountOpt = accountRepository.findByAccountId(accountId);
        Account account = accountOpt.orElseThrow(AccountNotFoundException::new);
        return new AccountDto(account.getAccountId(), account.getEmail(), List.of());
    }

    @Override
    public List<AccountDto> getAccounts() {
        return StreamSupport.stream(accountRepository.findAll().spliterator(), false)
                .map(account -> new AccountDto(account.getAccountId(), account.getEmail(), List.of()))
                .collect(Collectors.toList());
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account accountToSave = new Account(accountDto.getAccountId(), accountDto.getEmail());
        accountToSave.setAccountId(UUID.randomUUID().toString());
        accountToSave = accountRepository.save(accountToSave);
        return new AccountDto(accountToSave.getAccountId(), accountToSave.getEmail(), List.of());
    }
}
