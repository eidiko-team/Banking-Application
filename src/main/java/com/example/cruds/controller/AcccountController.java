package com.example.cruds.controller;

import com.example.cruds.dto.AccountRequestDTO;
import com.example.cruds.dto.AccountResponseDTO;
import com.example.cruds.models.Account;
import com.example.cruds.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@RestController
public class AcccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/add")
    public ResponseEntity<AccountResponseDTO> add(
            @RequestBody AccountRequestDTO request) {

        AccountResponseDTO response = accountService.add(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AccountResponseDTO>> getAll() {

        List<AccountResponseDTO> accounts = accountService.getAll();

        return ResponseEntity.ok(accounts);
    }
}
