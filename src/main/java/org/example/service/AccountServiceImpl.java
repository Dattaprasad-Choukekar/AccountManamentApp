package org.example.service;

import jakarta.annotation.PostConstruct;
import org.example.dto.AccountDto;
import org.example.exception.AccountNotFoundException;
import org.example.model.Account;
import org.example.repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class AccountServiceImpl implements AccountService {
    private ModelMapper mapper = new ModelMapper();

    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDto getAccount(String accountId) {
        Optional<Account> accountOpt = accountRepository.findByAccountId(accountId);
        Account account = accountOpt.orElseThrow(AccountNotFoundException::new);
        return toAccountDto(account);
    }

    @PostConstruct
    private void init() {
        mapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
    }

    @Override
    public List<AccountDto> getAccounts() {
        return iterableToStream(accountRepository.findAll())
                .map(this::toAccountDto)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account accountToSave = mapper.map(accountDto, Account.class);
        accountToSave.setAccountId(UUID.randomUUID().toString());

        accountToSave = accountRepository.save(accountToSave);

        return toAccountDto(accountToSave);
    }

    private AccountDto toAccountDto(Account accountToSave) {
        return mapper.map(accountToSave, AccountDto.class);
    }

    private static <T> Stream<T> iterableToStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
