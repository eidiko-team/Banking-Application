package com.example.cruds.services;

import com.example.cruds.dto.*;
import com.example.cruds.exceptions.CustomerNotFoundException;
import com.example.cruds.models.*;
import com.example.cruds.repo.AccountRepo;
import com.example.cruds.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private AccountRepo accountRepo;


    // ADD CUSTOMER
    public CustomerResponseDTO addCustomer(CustomerRequestDTO request) {

        Customer customer = new Customer();

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());

        Customer savedCustomer = customerRepo.save(customer);

        return convertToResponseDTO(savedCustomer);
    }


    // GET CUSTOMER
    public CustomerResponseDTO getCustomer(Long id) {

        Customer customer = customerRepo.findById(id).orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with id " + id
                        ));

        return convertToResponseDTO(customer);
    }


    // DELETE CUSTOMER
    public String delete(Long id) {

        Customer customer = customerRepo.findById(id).orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with id " + id
                        ));

        customerRepo.delete(customer);

        return "Customer deleted successfully with id " + id;
    }


    // UPDATE CUSTOMER
    public CustomerResponseDTO update(
            Long id,
            CustomerRequestDTO request) {

        Customer customer = customerRepo.findById(id).orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with id " + id
                        ));

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());

        Customer updatedCustomer =
                customerRepo.save(customer);

        return convertToResponseDTO(updatedCustomer);
    }


    // GET ALL CUSTOMERS
    public List<CustomerResponseDTO> getAll() {

        List<Customer> customers = customerRepo.findAll();

        List<CustomerResponseDTO> response =
                new ArrayList<>();

        for (Customer customer : customers) {

            CustomerResponseDTO dto =
                    convertToResponseDTO(customer);

            response.add(dto);
        }

        return response;
    }


    // GET ALL ACCOUNTS OF CUSTOMER
    public List<AccountResponseDTO> getAllAccountsOfCustomer(
            Long customerId) {

        Customer customer = customerRepo.findById(customerId).orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with id " + customerId
                        ));

        List<Account> accounts =
                customer.getAccounts();

        List<AccountResponseDTO> response =
                new ArrayList<>();

        for (Account account : accounts) {

            AccountResponseDTO dto =
                    new AccountResponseDTO();

            dto.setAccountId(account.getAccountId());
            dto.setAccountNumber(account.getAccountNumber());
            dto.setAccountType(account.getAccountType());
            dto.setBalance(account.getBalance());
            dto.setStatus(account.getStatus());
            dto.setCustomerId(customer.getCustomerId());

            response.add(dto);
        }

        return response;
    }


    // ENTITY → CUSTOMER RESPONSE DTO
    private CustomerResponseDTO convertToResponseDTO(
            Customer customer) {

        CustomerResponseDTO response =
                new CustomerResponseDTO();

        List<LoanResponseDTO> loanResponseDTOList = new ArrayList<>();

        if(customer.getLoans()!=null){
            for(Loan loan:customer.getLoans()){
                LoanResponseDTO loanResponseDTO = new LoanResponseDTO();

                loanResponseDTO.setCustomerId(loan.getCustomer().getCustomerId());
                loanResponseDTO.setStatus(loan.getStatus());
                loanResponseDTO.setLoanAmount(loan.getLoanAmount());
                loanResponseDTO.setLoanType(loan.getLoanType());
                loanResponseDTO.setLoanId(loan.getLoanId());
                loanResponseDTO.setInterestRate(loan.getInterestRate());
                loanResponseDTO.setTenureMonths(loan.getTenureMonths());

                loanResponseDTOList.add(loanResponseDTO);
            }
        }

        response.setLoans(loanResponseDTOList);

        response.setCustomerId(
                customer.getCustomerId()
        );

        response.setName(
                customer.getName()
        );

        response.setEmail(
                customer.getEmail()
        );

        response.setPhone(
                customer.getPhone()
        );

        response.setAddress(
                customer.getAddress()
        );


        // Convert Accounts
        List<AccountResponseDTO> accountDTOs =
                new ArrayList<>();

        if (customer.getAccounts() != null) {

            for (Account account : customer.getAccounts()) {

                AccountResponseDTO accountDTO =
                        new AccountResponseDTO();

                accountDTO.setAccountId(
                        account.getAccountId()
                );

                accountDTO.setAccountNumber(
                        account.getAccountNumber()
                );

                accountDTO.setAccountType(
                        account.getAccountType()
                );

                accountDTO.setBalance(
                        account.getBalance()
                );

                accountDTO.setStatus(
                        account.getStatus()
                );

                accountDTO.setCustomerId(
                        customer.getCustomerId()
                );

                accountDTOs.add(accountDTO);
            }
        }

        response.setAccounts(accountDTOs);

        //convert LoanOffers

        List<LoanProductResponseDTO> loanOffers= new ArrayList<>();

        if(customer.getLoanProducts()!=null){
            for(LoanProduct loanProduct:customer.getLoanProducts()){

                LoanProductResponseDTO loanProductResponseDTO = new LoanProductResponseDTO();

                loanProductResponseDTO.setProductName(loanProduct.getProductName());
                loanProductResponseDTO.setProductId(loanProduct.getProductId());
                loanProductResponseDTO.setInterestRate(loanProduct.getInterestRate());
                loanProductResponseDTO.setMaxTenureMonths(loanProduct.getMaxTenureMonths());
                loanOffers.add(loanProductResponseDTO);
            }
        }

        response.setLoanOffers(loanOffers);


        // Convert KYC
        if (customer.getKyc() != null) {

            Kyc kyc = customer.getKyc();

            KycResponseDTO kycDTO =
                    new KycResponseDTO();

            kycDTO.setKyc_id(
                    kyc.getKyc_id()
            );

            kycDTO.setAadhaarNumber(
                    kyc.getAadhaarNumber()
            );

            kycDTO.setPanNumber(
                    kyc.getPanNumber()
            );

            kycDTO.setVerificationStatus(
                    kyc.getVerificationStatus()
            );

            kycDTO.setCustomerid(
                    customer.getCustomerId()
            );

            response.setKyc(kycDTO);
        }

        return response;
    }
}