package com.example.contact.dto;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class VariablesDTO {
    @Value("${email_api_key}")
    private String emailAPI;
    @Value("${phone_api_key}")
    private String phoneAPI;
}
