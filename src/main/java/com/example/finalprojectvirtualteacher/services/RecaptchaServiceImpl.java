package com.example.finalprojectvirtualteacher.services;

import com.example.finalprojectvirtualteacher.exceptions.InvalidRecaptchaException;
import com.example.finalprojectvirtualteacher.models.RecaptchaResponse;
import com.example.finalprojectvirtualteacher.services.contacts.RecaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.POST;

@Service
public class RecaptchaServiceImpl implements RecaptchaService {

    public static final String RECAPTCHA_SECRET_ATT = "recaptcha.secret";
    public static final String RECAPTCHA_API = "https://www.google.com/recaptcha/api/siteverify";
    public static final String QUERY_SECRET = "?secret=";
    public static final String AND_RESPONSE = "&response=";

    private final String recaptchaSecret;
    private final RestTemplate restTemplate;

    @Autowired
    public RecaptchaServiceImpl(Environment environment,
                                      RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        recaptchaSecret = environment.getProperty(RECAPTCHA_SECRET_ATT);
    }
    @Override
    public void validateRecaptcha(String response) {
        RecaptchaResponse recaptchaResponse = getResponse(response);
        if (!recaptchaResponse.isSuccess()) {
            throw new InvalidRecaptchaException("You are a robot");
        }
    }

    private RecaptchaResponse getResponse(String response) {
        final String recaptchaUrlAndParams = RECAPTCHA_API + QUERY_SECRET + recaptchaSecret + AND_RESPONSE + response;
        ResponseEntity<RecaptchaResponse> exchange = restTemplate.exchange(recaptchaUrlAndParams,
                POST, null, RecaptchaResponse.class);
        return exchange.getBody();
    }

    @Override
    public boolean verify(String response) {
        RecaptchaResponse recaptchaResponse = getResponse(response);
        return recaptchaResponse.isSuccess();
    }

}

