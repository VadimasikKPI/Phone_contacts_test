package com.example.contact.service;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class PhoneNumberValidator {
    private static final String PHONE_NUMBER_VALIDATION_API_URL = "http://apilayer.net/api/validate?access_key=";

    public static boolean isPhoneNumberValid(String phoneNumber, String apiKey) throws IOException {
        String apiUrl = PHONE_NUMBER_VALIDATION_API_URL + apiKey + "&number=" + phoneNumber + "&format=1";

        HttpGet request = new HttpGet(apiUrl);

        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        String responseJson = EntityUtils.toString(entity);

        JsonObject validationDTO = new Gson().fromJson(responseJson, JsonObject.class);
        boolean isValid = validationDTO.get("valid").getAsBoolean();
        System.out.println("ISDVALID - " + isValid);
        return isValid;
    }
}
