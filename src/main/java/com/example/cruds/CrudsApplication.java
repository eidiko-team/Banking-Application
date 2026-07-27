package com.example.cruds;

import com.example.cruds.services.AccountService;
import com.example.cruds.services.LoanService;
import com.example.cruds.services.TransactionService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:bean.xml")
public class CrudsApplication {

	public static void main(String[] args) {


		ApplicationContext context = SpringApplication.run(CrudsApplication.class, args);

		AccountService accountService = context.getBean(AccountService.class);

		TransactionService transactionService = context.getBean(TransactionService.class);


		LoanService loanService = context.getBean(LoanService.class);

		loanService.display();

		transactionService.service();

		accountService.message();



	}

}
