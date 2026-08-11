package com.example.cruds.controller;

import com.example.cruds.dto.KycRequestDTO;
import com.example.cruds.dto.KycResponseDTO;
import com.example.cruds.services.KycService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kyc")
public class KycController {

    @Autowired
    private KycService kycService;

    @PostMapping("/create")
    public ResponseEntity<KycResponseDTO> createKyc(
            @RequestBody KycRequestDTO request) {

        KycResponseDTO response = kycService.createKyc(request);

        return ResponseEntity.ok(response);
    }
}