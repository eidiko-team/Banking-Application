package com.example.cruds.services;

import com.example.cruds.exceptions.CustomerNotFoundException;
import com.example.cruds.models.Customer;
import com.example.cruds.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    public String addCustomer(Customer customer){
        Customer customer1 = customerRepo.save(customer);
        return "Customer has been added successfully";
    }

    public Customer getCustomer(Long id){
        try {
            Customer customer = customerRepo.findById(id).get();
            return customer;
        }catch (Exception e){
            throw new CustomerNotFoundException("customer not fount");
        }

    }

    public String delete(Long id){
        customerRepo.deleteById(id);
        return "Customer deleted successfully with id "+id;
    }

    public Customer update(Long id, Customer customer){
        Customer customer1 = customerRepo.findById(id).get();

//        customer1.builder()
//                .email(customer.getEmail())
//                .phone(customer.getPhone())
//                .address(customer.getAddress())
//                .build();

        customer1.setEmail(customer.getEmail());

        customerRepo.save(customer1);
        return customer1;
    }

    public List<Customer> getAll(){
        return customerRepo.findAll();
    }


}
