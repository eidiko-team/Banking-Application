package com.example.cruds.controller;

import com.example.cruds.models.Customer;
import com.example.cruds.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
//@Controller
//@ResponseBody
@RequestMapping("/customer")
@Tag(name = "Customer Controller", description = "Customer Management APIs")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Operation(
            summary = "Add Customer",
            description = "Creates a new customer in the database."
    )
    @ApiResponse(responseCode = "200", description = "Customer added successfully")
    @PostMapping("/addCustomer")
    public String addCustomer(@Valid @RequestBody Customer customer){
        customerService.addCustomer(customer);
        return "Customer Added Successfully";
    }

    @Operation(
            summary = "Get Customer",
            description = "Fetch customer details using customer ID."
    )
    @ApiResponse(responseCode = "200", description = "Customer found")
    @ApiResponse(responseCode = "404", description = "Customer not found")
    @GetMapping("/get")
    public Customer getCustomer(
            @Parameter(description = "Customer ID", example = "1")
            @RequestParam Long id){
        return customerService.getCustomer(id);
    }

    @Operation(
            summary = "Delete Customer",
            description = "Deletes a customer by ID."
    )
    @ApiResponse(responseCode = "200", description = "Customer deleted successfully")
    @DeleteMapping("/delete")
    public String delete(
            @Parameter(description = "Customer ID", example = "1")
            @RequestParam Long id){
        return customerService.delete(id);
    }

    @Operation(
            summary = "Update Customer",
            description = "Updates customer information."
    )
    @ApiResponse(responseCode = "200", description = "Customer updated successfully")
    @PutMapping("/update")
    public Customer update(
            @Parameter(description = "Customer ID", example = "1")
            @RequestParam Long id,
            @RequestBody Customer customer){
        return customerService.update(id, customer);
    }

    @Operation(
            summary = "Get All Customers",
            description = "Returns all customers."
    )
    @ApiResponse(responseCode = "200", description = "Customers retrieved successfully")
    @GetMapping("/getAll")
    public List<Customer> getAll(){
        return customerService.getAll();
    }
}