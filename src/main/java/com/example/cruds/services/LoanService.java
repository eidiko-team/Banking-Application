package com.example.cruds.services;

//public class LoanService {
//
//    public void display(){
//        System.out.println( "************* I am using XML-based configuration ***************");
//    }
//}



import com.example.cruds.dto.LoanRequestDTO;
import com.example.cruds.dto.LoanResponseDTO;
import com.example.cruds.exceptions.CustomerNotFoundException;
import com.example.cruds.models.Customer;
import com.example.cruds.models.Loan;
import com.example.cruds.repo.CustomerRepo;
import com.example.cruds.repo.LoanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//@Service
public class LoanService {

        public void display(){
        System.out.println( "************* I am using XML-based configuration ***************");
    }

    @Autowired
    private LoanRepo loanRepo;

    @Autowired
    private CustomerRepo customerRepo;

    public LoanResponseDTO createLoan(LoanRequestDTO request) {

        Customer customer = customerRepo.findById(request.getCustomerId())
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        Loan loan = new Loan();

        loan.setLoanType(request.getLoanType());
        loan.setLoanAmount(request.getLoanAmount());
        loan.setInterestRate(request.getInterestRate());
        loan.setTenureMonths(request.getTenureMonths());
        loan.setStatus(request.getStatus());

        // Associate customer with loan
        loan.setCustomer(customer);

        Loan savedLoan = loanRepo.save(loan);

        LoanResponseDTO response = new LoanResponseDTO();

        response.setLoanId(savedLoan.getLoanId());
        response.setLoanType(savedLoan.getLoanType());
        response.setLoanAmount(savedLoan.getLoanAmount());
        response.setInterestRate(savedLoan.getInterestRate());
        response.setTenureMonths(savedLoan.getTenureMonths());
        response.setStatus(savedLoan.getStatus());
        response.setCustomerId(savedLoan.getCustomer().getCustomerId());

        return response;
    }
}
