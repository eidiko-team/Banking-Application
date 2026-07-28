package com.example.cruds;

import com.example.cruds.services.AccountService;
import com.example.cruds.services.TransactionService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class CrudsApplication {

	public static void main(String[] args) {


		ApplicationContext context = SpringApplication.run(CrudsApplication.class, args);

		AccountService accountService = context.getBean(AccountService.class);

		TransactionService transactionService = context.getBean(TransactionService.class);

		//tested that model class can also acts as bean when it configured with @cpmponent
//        Customer customer = context.getBean(Customer.class);
//		customer.displayy();

		transactionService.service();

		accountService.message();
	}

}
