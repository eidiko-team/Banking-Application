package com.example.cruds.repo;

import com.example.cruds.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepo extends JpaRepository<Loan, Long> {

}
