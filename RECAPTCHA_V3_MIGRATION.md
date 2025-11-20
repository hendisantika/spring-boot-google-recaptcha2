# reCAPTCHA v2 to v3 Migration Guide

This document outlines the changes made to migrate from Google reCAPTCHA v2 to v3.

## What Changed

### 1. RecaptchaService.java

**Changes:**

- Added `@Slf4j` annotation for logging
- Added `SCORE_THRESHOLD` constant (default: 0.5)
- Updated `validateRecaptcha()` to check score instead of just success
- Added overloaded method with action verification parameter
- Added `getScore()` method to retrieve raw scores
- Added comprehensive error logging and validation logic

**Key Differences:**

```java
// v2: Simple boolean check
return Boolean.TRUE.equals(apiResponse.getSuccess());

// v3: Score-based validation with action verification
        if(apiResponse.

getScore() ==null||apiResponse.

getScore() <SCORE_THRESHOLD){
        return false;
        }
        if(expectedAction !=null&&!expectedAction.

equals(apiResponse.getAction())){
        return false;
        }
```

### 2. form.html

**Changes:**

- Removed visible reCAPTCHA v2 widget (`<div class="g-recaptcha">`)
- Added hidden input field for token storage
- Changed script source to v3 API with `render` parameter
- Added JavaScript to intercept form submission
- Implemented `grecaptcha.execute()` to generate tokens
- Added loading state to submit button
- Added reCAPTCHA privacy notice

**Key Differences:**

```html
<!-- v2: Visible widget -->
<div class="g-recaptcha" data-sitekey="SITE_KEY"></div>

<!-- v3: Hidden field + JavaScript execution -->
<input id="g-recaptcha-response" name="g-recaptcha-response" type="hidden"/>
<script>
    grecaptcha.execute(SITE_KEY, {action: 'submit'}).then(function (token) {
        document.getElementById('g-recaptcha-response').value = token;
        document.getElementById('employeeForm').submit();
    });
</script>
```

### 3. EmployeeController.java

**Changes:**

- Updated parameter name from `captcha` to `captchaToken` for clarity
- Added action parameter ("submit") to validation call
- Improved error message to be more user-friendly

**Key Differences:**

```java
// v2
boolean captchaValid = recaptchaService.validateRecaptcha(captcha);

// v3
boolean captchaValid = recaptchaService.validateRecaptcha(captchaToken, "submit");
```

### 4. RecaptchaResponse.java

**No Changes Required:**

- Already had `score` and `action` fields needed for v3

## Configuration Changes

### Google reCAPTCHA Console

1. **Create New Site:**
    - Go to [Google reCAPTCHA Admin](https://www.google.com/recaptcha/admin)
    - Register a **new** site with reCAPTCHA v3 (NOT v2)
    - Add your domains (including `localhost` for development)
    - Copy the new Site Key and Secret Key

2. **Update Keys:**
    - Replace Secret Key in `RecaptchaService.java`
    - Replace Site Key (2 places) in `form.html`

⚠️ **Important:** v2 and v3 keys are different and not interchangeable!

## Behavioral Changes

### User Experience

| Aspect       | v2                            | v3                    |
|--------------|-------------------------------|-----------------------|
| Visibility   | Visible checkbox or challenge | Completely invisible  |
| User Action  | Required to click/solve       | No action required    |
| Form Flow    | May interrupt                 | Seamless              |
| Loading Time | Instant                       | Brief delay on submit |

### Validation

| Aspect          | v2                 | v3                         |
|-----------------|--------------------|----------------------------|
| Result          | Pass/Fail (binary) | Score 0.0-1.0              |
| Threshold       | N/A                | Configurable (default 0.5) |
| False Positives | Lower              | May require tuning         |
| Action Tracking | No                 | Yes                        |

## Testing Considerations

### Development Testing

1. **Local Testing:**
    - Add `localhost` to allowed domains in reCAPTCHA console
    - Test with actual user interactions (bots will score low)
    - Check browser console for any JavaScript errors
    - Monitor application logs for score values

2. **Score Testing:**
   ```java
   // Temporarily add this to see scores:
   double score = recaptchaService.getScore(token);
   log.info("reCAPTCHA score: {}", score);
   ```

3. **Common Scores:**
    - Human interactions: typically 0.7-1.0
    - Automated testing: typically 0.1-0.3
    - Suspicious behavior: 0.3-0.6

### Threshold Tuning

Start with 0.5 and adjust based on:

- False positive rate (legitimate users blocked)
- False negative rate (bots getting through)
- Application security requirements

```java
// In RecaptchaService.java
private static final double SCORE_THRESHOLD = 0.5; // Adjust as needed

// Stricter (fewer bots, more false positives): 0.7-0.8
// Balanced (default): 0.5
// Lenient (fewer false positives, more bots): 0.3-0.4
```

## Troubleshooting

### "reCAPTCHA validation failed" Errors

1. Check application logs for score values
2. Verify Secret Key matches your v3 configuration
3. Ensure Site Key in form.html is correct
4. Verify domain is registered in console
5. Check action name matches ("submit")

### JavaScript Errors

1. Open browser console (F12)
2. Look for `grecaptcha` errors
3. Verify Site Key is correct in both places
4. Check network requests to Google's API

### Low Scores in Development

- Automated tests typically score low (0.1-0.3)
- VPNs may affect scores
- Bot-like behavior (fast submissions) scores low
- Consider using lower threshold for development

## Rollback Plan

If you need to revert to v2:

1. Restore `RecaptchaService.java` to simple boolean check
2. Restore `form.html` to use v2 widget
3. Update controller to remove action parameter
4. Use v2 keys from console

## Additional Resources

- [reCAPTCHA v3 Documentation](https://developers.google.com/recaptcha/docs/v3)
- [Admin Console](https://www.google.com/recaptcha/admin)
- [Score Interpretation Guide](https://developers.google.com/recaptcha/docs/v3#interpreting_the_score)

## Production Checklist

- [ ] Created reCAPTCHA v3 site in console
- [ ] Updated Secret Key in code
- [ ] Updated Site Key in form (both places)
- [ ] Added production domains to console
- [ ] Tested on actual domain (not localhost)
- [ ] Monitored scores in production
- [ ] Tuned threshold based on metrics
- [ ] Moved keys to environment variables
- [ ] Set up monitoring/alerts for failed validations
- [ ] Documented threshold decision for team
