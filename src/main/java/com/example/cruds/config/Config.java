package com.example.cruds.config;

import ch.qos.logback.core.model.Model;
import com.example.cruds.services.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {



    @Bean
    public TransactionService transactionService(){
        return new TransactionService();
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }



}
