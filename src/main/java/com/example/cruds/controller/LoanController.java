package com.example.cruds.controller;

import com.example.cruds.dto.LoanRequestDTO;
import com.example.cruds.dto.LoanResponseDTO;
import com.example.cruds.models.Loan;
import com.example.cruds.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;

//    @PostMapping("/add")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<LoanResponseDTO> addLoan(
            @RequestBody LoanRequestDTO request) {

        LoanResponseDTO response =
                loanService.createLoan(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public LoanResponseDTO getLoan(@PathVariable Long id) {
        return loanService.getLoan(id);

    }
}