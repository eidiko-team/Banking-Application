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
import com.example.cruds.exceptions.ResourceNotFoundException;
import com.example.cruds.models.Customer;
import com.example.cruds.models.Loan;
import com.example.cruds.repo.CustomerRepo;
import com.example.cruds.repo.LoanRepo;
import org.apache.catalina.connector.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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


    @Autowired
    private ModelMapper modelMapper;
    
    
    //use it for default intrest rate which i sin the configuration file
    @Value("${loan.default-interest-rate}")
    private Double defaultInterestRate;

    public LoanResponseDTO createLoan(LoanRequestDTO request) {

        Customer customer = customerRepo.findById(request.getCustomerId())
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        Loan loan = new Loan();

        loan.setLoanType(request.getLoanType());
        loan.setLoanAmount(request.getLoanAmount());
//        loan.setInterestRate(request.getInterestRate());
        loan.setInterestRate(defaultInterestRate);
        loan.setTenureMonths(request.getTenureMonths());
        loan.setStatus(request.getStatus());

        // Associate customer with loan
        loan.setCustomer(customer);

        Loan savedLoan = loanRepo.save(loan);

        LoanResponseDTO response = modelMapper.map(savedLoan, LoanResponseDTO.class);


//        LoanResponseDTO response = new LoanResponseDTO();
//
//        response.setLoanId(savedLoan.getLoanId());
//        response.setLoanType(savedLoan.getLoanType());
//        response.setLoanAmount(savedLoan.getLoanAmount());
//        response.setInterestRate(savedLoan.getInterestRate());
//        response.setTenureMonths(savedLoan.getTenureMonths());
//        response.setStatus(savedLoan.getStatus());
//        response.setCustomerId(savedLoan.getCustomer().getCustomerId());

        return response;
    }


//    public Loan getLoan(Long id){
//        return loanRepo.findById(id)
//                .orElseThrow(()->
//                        new ResourceNotFoundException("Loan not found with id "+id)
//                );
//    }

    public LoanResponseDTO getLoan(Long id) {

        try {

            Loan loan =  loanRepo.findById(id)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Loan not found with id: " + id
                            )
                    );

            LoanResponseDTO response = modelMapper.map(loan, LoanResponseDTO.class);
            return response;

        } catch (ResourceNotFoundException e) {

            throw e;

        } catch (Exception e) {
            //we can send cause in 2 ways
//////////////////////////////////////////////////////////////

           //            1. with init cause

//            ResourceNotFoundException ex =
//                    new ResourceNotFoundException(
//                            "Unable to retrieve loan with id: " + id
//                    );
//
//            ex.initCause(e);
//
//            throw ex;
////////////////////////////////////////////////////////////////////////


            //           Way 2 — Constructor
//            The constructor approach is generally cleaner and preferred
//            when you know the cause at the time you create the exception.
            throw new ResourceNotFoundException(
                    "Unable to retrieve loan with id: " + id,
                    e
            );
        }
    }



}
