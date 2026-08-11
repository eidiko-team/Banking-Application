package com.example.cruds.controller;

import com.example.cruds.dto.AccountResponseDTO;
import com.example.cruds.dto.CustomerRequestDTO;
import com.example.cruds.dto.CustomerResponseDTO;
import com.example.cruds.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@Tag(name = "Customer Controller", description = "Customer Management APIs")
public class CustomerController {

    @Autowired
    private CustomerService customerService;


    @Operation(
            summary = "Add Customer",
            description = "Creates a new customer in the database."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Customer added successfully"
    )
    @PostMapping("/addCustomer")
    public ResponseEntity<CustomerResponseDTO> addCustomer(
            @Valid @RequestBody CustomerRequestDTO request) {

        CustomerResponseDTO response =
                customerService.addCustomer(request);

        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Get Customer",
            description = "Fetch customer details using customer ID."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Customer found"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Customer not found"
    )
    @GetMapping("/get")
    public ResponseEntity<CustomerResponseDTO> getCustomer(
            @Parameter(description = "Customer ID", example = "1")
            @RequestParam Long id) {

        CustomerResponseDTO response =
                customerService.getCustomer(id);

        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Delete Customer",
            description = "Deletes a customer by ID."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Customer deleted successfully"
    )
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(
            @Parameter(description = "Customer ID", example = "1")
            @RequestParam Long id) {

        String response = customerService.delete(id);

        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Update Customer",
            description = "Updates customer information."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Customer updated successfully"
    )
    @PutMapping("/update")
    public ResponseEntity<CustomerResponseDTO> update(
            @Parameter(description = "Customer ID", example = "1")
            @RequestParam Long id,
            @Valid @RequestBody CustomerRequestDTO request) {

        CustomerResponseDTO response =
                customerService.update(id, request);

        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Get All Customers",
            description = "Returns all customers."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Customers retrieved successfully"
    )
    @GetMapping("/getAll")
    public ResponseEntity<List<CustomerResponseDTO>> getAll() {

        List<CustomerResponseDTO> response =
                customerService.getAll();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/getAccounts")
    public ResponseEntity<List<AccountResponseDTO>> getAllAccountsOfCustomer(
            @RequestParam Long id) {

        List<AccountResponseDTO> accounts =
                customerService.getAllAccountsOfCustomer(id);

        return ResponseEntity.ok(accounts);
    }
}