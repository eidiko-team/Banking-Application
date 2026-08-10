package com.example.cruds.services;

import com.example.cruds.dto.AccountRequestDTO;
import com.example.cruds.dto.AccountResponseDTO;
import com.example.cruds.models.Account;
import com.example.cruds.models.Customer;
import com.example.cruds.repo.AccountRepo;
import com.example.cruds.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountService {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private CustomerRepo customerRepo;

    public AccountResponseDTO add(AccountRequestDTO request) {

        // 1. Find customer using customerId
        Customer customer = customerRepo.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // 2. Create Account entity
        Account account = new Account();

        account.setAccountNumber(request.getAccountNumber());
        account.setAccountType(request.getAccountType());
        account.setBalance(request.getBalance());
        account.setStatus(request.getStatus());

        // 3. Connect Account with Customer
        account.setCustomer(customer);

        // 4. Save Account
        Account savedAccount = accountRepo.save(account);

        // 5. Convert Entity → DTO
        return new AccountResponseDTO(
                savedAccount.getAccountId(),
                savedAccount.getAccountNumber(),
                savedAccount.getAccountType(),
                savedAccount.getBalance(),
                savedAccount.getStatus(),
                savedAccount.getCustomer().getCustomerId()
        );
    }

    public List<AccountResponseDTO> getAll() {

        List<Account> accounts = accountRepo.findAll();

        return accounts.stream()
                .map(account -> new AccountResponseDTO(
                        account.getAccountId(),
                        account.getAccountNumber(),
                        account.getAccountType(),
                        account.getBalance(),
                        account.getStatus(),
                        account.getCustomer().getCustomerId()
                ))
                .toList();
    }
}