package com.example.cruds.controller;

import com.example.cruds.models.Customer;
import com.example.cruds.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/addCustomer")
    public String addCustomer(@Valid @RequestBody Customer customer){
        customerService.addCustomer(customer);
        return "Customer Added Successfully";

    }

    @GetMapping("/get")
    public Customer getCustomer(@RequestParam Long id){
        Customer customer = customerService.getCustomer(id);
        return customer;
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam Long id){
        return customerService.delete(id);

    }

    @PutMapping("/update")
    public Customer update(@RequestParam Long id, @RequestBody Customer customer){
        return customerService.update(id,customer);

    }


}
