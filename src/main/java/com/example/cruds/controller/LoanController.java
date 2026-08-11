package com.example.cruds.controller;

import com.example.cruds.dto.LoanRequestDTO;
import com.example.cruds.dto.LoanResponseDTO;
import com.example.cruds.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping("/add")
    public ResponseEntity<LoanResponseDTO> addLoan(
            @RequestBody LoanRequestDTO request) {

        LoanResponseDTO response =
                loanService.createLoan(request);

        return ResponseEntity.ok(response);
    }
}