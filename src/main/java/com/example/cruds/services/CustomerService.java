package com.example.cruds.services;

import com.example.cruds.models.Customer;
import com.example.cruds.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    public String addCustomer(Customer customer){
        customerRepo.save(customer);
        return "Customer has been added successfully";
    }

}
