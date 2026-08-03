package com.example.cruds.zpractise;

import com.example.cruds.config.Config;
import com.example.cruds.services.AccountService;
import com.example.cruds.services.CustomerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Helloworld {

//    method overloading difference

    public int sum(int a, int b){
        return a+b;
    }

    public double sum(double a, int b){
        return (int)a+b;
    }

    public static void main(String[] args) {
        // Load Spring context using the configuration class
//        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

        // Retrieve the bean
//        AccountService userService = context.getBean(AccountService.class);

        // Call the bean method
//        userService.message();
    }
}


