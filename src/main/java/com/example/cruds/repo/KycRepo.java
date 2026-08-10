package com.example.cruds.repo;

import com.example.cruds.models.Kyc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KycRepo extends JpaRepository<Kyc,Long> {
}
