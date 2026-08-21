package com.example.cruds.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "bank")
public class BankProperties {

    private String name;

    private String code;

    private String supportEmail;

    private String supportPhone;

    private Integer maxLoginAttempts;
}