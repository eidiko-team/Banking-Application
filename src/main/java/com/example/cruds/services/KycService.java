package com.example.cruds.services;

import com.example.cruds.models.Kyc;
import com.example.cruds.repo.KycRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KycService {
    @Autowired
    private KycRepo kycRepo;

    public Kyc createKyc(Kyc kyc){

        return kycRepo.save(kyc);
    }
}