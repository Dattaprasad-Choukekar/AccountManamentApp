package org.example;

import org.example.repository.AccountRepository;
import org.example.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountManagementApplication {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    public static void main(String[] args) {
        SpringApplication.run(AccountManagementApplication.class, args);
    }

/*    @Bean
    public CommandLineRunner commandLineRunner() {
        return (args) -> {
            Account account = accountRepository.save(new Account(UUID.randomUUID().toString(), "admin" +
                    "@email.com"));
            Transaction transaction = transactionRepository.save(new Transaction(account, UUID.randomUUID().toString(), BigDecimal.TEN, Transaction.TransactionType.DEBIT));
        };
    }*/
}