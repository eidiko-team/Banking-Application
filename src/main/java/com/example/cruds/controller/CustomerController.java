package com.example.cruds.controller;

import com.example.cruds.models.Customer;
import com.example.cruds.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/addCustomer")
    public String addCustomer(@RequestBody Customer customer){
        customerService.addCustomer(customer);
        return "Customer Added Successfully";

    }

}
