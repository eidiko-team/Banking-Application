package com.example.cruds.services;

import com.example.cruds.dto.KycRequestDTO;
import com.example.cruds.dto.KycResponseDTO;
import com.example.cruds.exceptions.CustomerNotFoundException;
import com.example.cruds.models.Customer;
import com.example.cruds.models.Kyc;
import com.example.cruds.repo.CustomerRepo;
import com.example.cruds.repo.KycRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KycService {

    @Autowired
    private KycRepo kycRepo;

    @Autowired
    private CustomerRepo customerRepo;

    public KycResponseDTO createKyc(KycRequestDTO request) {

        // 1. Find customer using customer ID
        Customer customer = customerRepo.findById(request.getCustomerid())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        // 2. Create Kyc entity
        Kyc kyc = new Kyc();

        // 3. Set values from DTO to Entity
        kyc.setAadhaarNumber(request.getAadhaarNumber());
        kyc.setPanNumber(request.getPanNumber());
        kyc.setVerificationStatus(request.getVerificationStatus());

        // 4. Set Customer
        kyc.setCustomer(customer);

        // 5. Save KYC
        Kyc savedKyc = kycRepo.save(kyc);

        // 6. Entity → Response DTO
        KycResponseDTO response = new KycResponseDTO();

        response.setKyc_id(savedKyc.getKyc_id());
        response.setAadhaarNumber(savedKyc.getAadhaarNumber());
        response.setPanNumber(savedKyc.getPanNumber());
        response.setVerificationStatus(savedKyc.getVerificationStatus());
        response.setCustomerid(savedKyc.getCustomer().getCustomerId());

        return response;
    }
}