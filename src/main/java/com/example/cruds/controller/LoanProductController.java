package com.example.cruds.controller;

import com.example.cruds.dto.LoanProductRequestDTO;
import com.example.cruds.dto.LoanProductResponseDTO;
import com.example.cruds.services.LoanProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loanProduct")
public class LoanProductController {

    @Autowired
    private LoanProductService loanProductService;


    // Create Loan Product
    @PostMapping("/add")
    public ResponseEntity<LoanProductResponseDTO> addLoanProduct(
            @RequestBody LoanProductRequestDTO request) {

        LoanProductResponseDTO response =
                loanProductService.createLoanProduct(request);

        return ResponseEntity.ok(response);
    }


    // Assign Loan Product to Customer
    @PostMapping("/assign")
    public ResponseEntity<String> assignLoanProduct(
            @RequestParam Long customerId,
            @RequestParam Long productId) {

        String response =
                loanProductService.assignLoanProductToCustomer(
                        customerId,
                        productId
                );

        return ResponseEntity.ok(response);
    }


    // Get all Loan Products
    @GetMapping("/getAll")
    public ResponseEntity<List<LoanProductResponseDTO>>
    getAllLoanProducts() {

        return ResponseEntity.ok(
                loanProductService.getAllLoanProducts()
        );
    }


    // Get Loan Products of a Customer
    @GetMapping("/customer")
    public ResponseEntity<List<LoanProductResponseDTO>>
    getLoanProductsOfCustomer(
            @RequestParam Long customerId) {

        return ResponseEntity.ok(
                loanProductService
                        .getLoanProductsOfCustomer(customerId)
        );
    }
}