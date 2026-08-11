package com.example.cruds.services;

import com.example.cruds.dto.LoanProductRequestDTO;
import com.example.cruds.dto.LoanProductResponseDTO;
import com.example.cruds.exceptions.CustomerNotFoundException;
import com.example.cruds.models.Customer;
import com.example.cruds.models.LoanProduct;
import com.example.cruds.repo.CustomerRepo;
import com.example.cruds.repo.LoanProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoanProductService {

    @Autowired
    private LoanProductRepo loanProductRepo;

    @Autowired
    private CustomerRepo customerRepo;


    // Create Loan Product
    public LoanProductResponseDTO createLoanProduct(
            LoanProductRequestDTO request) {

        LoanProduct loanProduct = new LoanProduct();

        loanProduct.setProductName(request.getProductName());
        loanProduct.setInterestRate(request.getInterestRate());
        loanProduct.setMaxTenureMonths(request.getMaxTenureMonths());

        LoanProduct savedProduct =
                loanProductRepo.save(loanProduct);

        LoanProductResponseDTO response =
                new LoanProductResponseDTO();

        response.setProductId(savedProduct.getProductId());
        response.setProductName(savedProduct.getProductName());
        response.setInterestRate(savedProduct.getInterestRate());
        response.setMaxTenureMonths(savedProduct.getMaxTenureMonths());

        return response;
    }


    // Associate Loan Product with Customer
    public String assignLoanProductToCustomer(
            Long customerId,
            Long productId) {

        Customer customer = customerRepo.findById(customerId).orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found"));

        LoanProduct loanProduct = loanProductRepo.findById(productId).orElseThrow(() ->
                        new RuntimeException(
                                "Loan product not found"));

        loanProduct.getCustomers().add(customer);

        loanProductRepo.save(loanProduct);

        return "Loan product assigned to customer successfully";
    }


    // Get all Loan Products
    public List<LoanProductResponseDTO> getAllLoanProducts() {

        List<LoanProduct> products =
                loanProductRepo.findAll();

        List<LoanProductResponseDTO> response =
                new ArrayList<>();

        for (LoanProduct product : products) {

            LoanProductResponseDTO dto =
                    new LoanProductResponseDTO();

            dto.setProductId(product.getProductId());
            dto.setProductName(product.getProductName());
            dto.setInterestRate(product.getInterestRate());
            dto.setMaxTenureMonths(product.getMaxTenureMonths());

            response.add(dto);
        }

        return response;
    }


    // Get Loan Products of a Customer
    public List<LoanProductResponseDTO> getLoanProductsOfCustomer(
            Long customerId) {

        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found"));

        List<LoanProduct> products =
                customer.getLoanProducts();

        List<LoanProductResponseDTO> response =
                new ArrayList<>();

        for (LoanProduct product : products) {

            LoanProductResponseDTO dto =
                    new LoanProductResponseDTO();

            dto.setProductId(product.getProductId());
            dto.setProductName(product.getProductName());
            dto.setInterestRate(product.getInterestRate());
            dto.setMaxTenureMonths(product.getMaxTenureMonths());

            response.add(dto);
        }

        return response;
    }
}