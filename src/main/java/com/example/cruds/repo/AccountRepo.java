package com.example.cruds.repo;

import com.example.cruds.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public interface AccountRepo extends JpaRepository<Account,Long> {
}
