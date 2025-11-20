package com.hendisantika.recaptcha3.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-google-recaptcha2
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 12/11/23
 * Time: 05:30
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@ConfigurationProperties(prefix = "recaptcha")
@Data
public class RecaptchaConfig {

    /**
     * reCAPTCHA v3 Secret Key (server-side validation)
     * Set via environment variable: RECAPTCHA_SECRET_KEY
     */
    private String secretKey;

    /**
     * reCAPTCHA v3 Site Key (client-side rendering)
     * Set via environment variable: RECAPTCHA_SITE_KEY
     */
    private String siteKey;

    /**
     * Score threshold for bot detection (0.0 - 1.0)
     * Higher values are stricter. Default: 0.5
     * Set via environment variable: RECAPTCHA_THRESHOLD (optional)
     */
    private double threshold = 0.5;
}
