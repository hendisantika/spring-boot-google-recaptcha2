package com.hendisantika.recaptcha3.service;

import com.hendisantika.recaptcha3.config.RecaptchaConfig;
import com.hendisantika.recaptcha3.response.RecaptchaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-google-recaptcha2
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 12/11/23
 * Time: 05:27
 * To change this template use File | Settings | File Templates.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RecaptchaService {
    private static final String GOOGLE_RECAPTCHA_ENDPOINT = "https://www.google.com/recaptcha/api/siteverify";

    private final RecaptchaConfig recaptchaConfig;

    /**
     * Validates reCAPTCHA v3 token and returns true if the score is above threshold
     *
     * @param token the reCAPTCHA token from the client
     * @return true if validation succeeds and score is above threshold, false otherwise
     */
    public boolean validateRecaptcha(String token) {
        return validateRecaptcha(token, null);
    }

    /**
     * Validates reCAPTCHA v3 token with optional action verification
     *
     * @param token          the reCAPTCHA token from the client
     * @param expectedAction the expected action name (e.g., "submit", "login")
     * @return true if validation succeeds, score is above threshold, and action matches
     */
    public boolean validateRecaptcha(String token, String expectedAction) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
            request.add("secret", recaptchaConfig.getSecretKey());
            request.add("response", token);

            RecaptchaResponse apiResponse = restTemplate.postForObject(
                    GOOGLE_RECAPTCHA_ENDPOINT,
                    request,
                    RecaptchaResponse.class
            );

            if (apiResponse == null) {
                log.error("reCAPTCHA API returned null response");
                return false;
            }

            log.info("reCAPTCHA validation result - Success: {}, Score: {}, Action: {}",
                    apiResponse.getSuccess(), apiResponse.getScore(), apiResponse.getAction());

            // Check if the request was successful
            if (!Boolean.TRUE.equals(apiResponse.getSuccess())) {
                log.warn("reCAPTCHA validation failed");
                return false;
            }

            // Check if score is above threshold
            if (apiResponse.getScore() == null || apiResponse.getScore() < recaptchaConfig.getThreshold()) {
                log.warn("reCAPTCHA score {} is below threshold {}", apiResponse.getScore(), recaptchaConfig.getThreshold());
                return false;
            }

            // Optionally verify the action if provided
            if (expectedAction != null && !expectedAction.equals(apiResponse.getAction())) {
                log.warn("reCAPTCHA action mismatch. Expected: {}, Got: {}", expectedAction, apiResponse.getAction());
                return false;
            }

            return true;

        } catch (Exception e) {
            log.error("Error validating reCAPTCHA: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Gets the score from reCAPTCHA validation without boolean result
     *
     * @param token the reCAPTCHA token from the client
     * @return the score (0.0 to 1.0) or -1.0 if validation fails
     */
    public double getScore(String token) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
            request.add("secret", recaptchaConfig.getSecretKey());
            request.add("response", token);

            RecaptchaResponse apiResponse = restTemplate.postForObject(
                    GOOGLE_RECAPTCHA_ENDPOINT,
                    request,
                    RecaptchaResponse.class
            );

            if (apiResponse != null && Boolean.TRUE.equals(apiResponse.getSuccess())) {
                return apiResponse.getScore() != null ? apiResponse.getScore() : -1.0;
            }

            return -1.0;
        } catch (Exception e) {
            log.error("Error getting reCAPTCHA score: {}", e.getMessage());
            return -1.0;
        }
    }
}
