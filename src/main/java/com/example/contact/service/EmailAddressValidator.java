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

public class EmailAddressValidator {
    private static final String EMAIL_VALIDATION_API_URL = "https://emailvalidation.abstractapi.com/v1/?api_key=";

    public static boolean isEmailValid(String email, String apiKey) throws IOException {
        String apiUrl = EMAIL_VALIDATION_API_URL + apiKey + "&email=" + email;

        HttpGet request = new HttpGet(apiUrl);

        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        String responseJson = EntityUtils.toString(entity);
        JsonObject validationDTO = new Gson().fromJson(responseJson, JsonObject.class);
        boolean isValid = validationDTO.getAsJsonObject("is_valid_format").get("value").getAsBoolean();

        return isValid;
    }
}
