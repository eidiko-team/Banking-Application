package com.example.cruds.repo;

import com.example.cruds.models.LoanProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanProductRepo extends JpaRepository<LoanProduct,Long> {
}
