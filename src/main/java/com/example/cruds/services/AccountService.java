package com.example.cruds.services;

import com.example.cruds.dto.AccountRequestDTO;
import com.example.cruds.dto.AccountResponseDTO;
import com.example.cruds.exceptions.ResourceNotFoundException;
import com.example.cruds.models.Account;
import com.example.cruds.models.Customer;
import com.example.cruds.repo.AccountRepo;
import com.example.cruds.repo.CustomerRepo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
public class AccountService {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private ModelMapper mapper;


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

    public AccountResponseDTO getAccount(Long id){
        log.trace("Entering getAccount()");
        log.info("Fetching Account with ID: {}", id);
        Account account = accountRepo.findById(id)
                .orElseThrow(

                        ()-> {
                            log.warn("Account not found with ID: {}",id);
                            return new ResourceNotFoundException("Account Not Found with id " + id);
                        });

        log.debug("Account found successfully with ID: {}", id);
        AccountResponseDTO response = mapper.map(account, AccountResponseDTO.class);
        return response;

    }

    public List<AccountResponseDTO> getAll() {

        log.trace("Entering getAccounts()");

        List<Account> accounts = accountRepo.findAll();
        log.info("Fetching Accounts");

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