# Environment Variable Setup Guide

This guide explains how reCAPTCHA keys are now managed as environment variables in the application.

## Why Environment Variables?

- **Security**: Keys are not stored in source code
- **Flexibility**: Easy to change keys without code changes
- **Best Practice**: Industry standard for managing secrets
- **Multi-Environment**: Different keys for dev, staging, production

## What Changed

### Before (Hardcoded)

```java
private final String RECAPTCHA_SECRET = "6LfKJeAmAAAAANxgKoJ3TmVqyKsR9TkXZEmgmiqf";
```

```html
<script src="https://www.google.com/recaptcha/api.js?render=YOUR_SITE_KEY"></script>
```

### After (Environment Variables)

```java
@Configuration
@ConfigurationProperties(prefix = "recaptcha")
public class RecaptchaConfig {
    private String secretKey;  // Read from RECAPTCHA_SECRET_KEY
    private String siteKey;    // Read from RECAPTCHA_SITE_KEY
    private final double threshold = 0.5;
}
```

## Required Environment Variables

| Variable               | Purpose                    | Example         |
|------------------------|----------------------------|-----------------|
| `RECAPTCHA_SECRET_KEY` | Server-side validation key | `6LcExample...` |
| `RECAPTCHA_SITE_KEY`   | Client-side rendering key  | `6LdExample...` |
| `RECAPTCHA_THRESHOLD`  | Score threshold (optional) | `0.5`           |

## Setup Methods

### 1. Using .env File (Development)

**Step 1:** Create `.env` file in project root

```bash
cp .env.example .env
```

**Step 2:** Edit `.env` with your keys

```bash
RECAPTCHA_SECRET_KEY=your_actual_secret_key
RECAPTCHA_SITE_KEY=your_actual_site_key
RECAPTCHA_THRESHOLD=0.5
```

**Step 3:** Load variables before running

**Linux/Mac:**

```bash
export $(cat .env | xargs)
mvn spring-boot:run
```

**Windows:**

```powershell
Get-Content .env | ForEach-Object {
    $name, $value = $_.split('=')
    Set-Content env:\$name $value
}
mvn spring-boot:run
```

### 2. IntelliJ IDEA Configuration

**Step 1:** Open Run/Debug Configurations

- Run → Edit Configurations...

**Step 2:** Add Environment Variables

- Select your Spring Boot application
- Environment variables field:
  ```
  RECAPTCHA_SECRET_KEY=your_key;RECAPTCHA_SITE_KEY=your_key;RECAPTCHA_THRESHOLD=0.5
  ```

**Step 3:** Apply and Run

### 3. Command Line Arguments

```bash
java -jar target/recaptcha2-0.0.1-SNAPSHOT.jar \
  --recaptcha.secret-key=your_secret_key \
  --recaptcha.site-key=your_site_key \
  --recaptcha.threshold=0.5
```

### 4. System Environment Variables

**Linux/Mac (persistent):**

```bash
# Add to ~/.bashrc or ~/.zshrc
export RECAPTCHA_SECRET_KEY="your_secret_key"
export RECAPTCHA_SITE_KEY="your_site_key"
export RECAPTCHA_THRESHOLD="0.5"

# Reload shell
source ~/.bashrc
```

**Windows (persistent):**

```powershell
# Set system environment variables
[System.Environment]::SetEnvironmentVariable("RECAPTCHA_SECRET_KEY", "your_secret_key", "User")
[System.Environment]::SetEnvironmentVariable("RECAPTCHA_SITE_KEY", "your_site_key", "User")
[System.Environment]::SetEnvironmentVariable("RECAPTCHA_THRESHOLD", "0.5", "User")
```

### 5. Docker

**docker-compose.yml:**

```yaml
version: '3.8'
services:
  app:
    build: .
    environment:
      - RECAPTCHA_SECRET_KEY=${RECAPTCHA_SECRET_KEY}
      - RECAPTCHA_SITE_KEY=${RECAPTCHA_SITE_KEY}
      - RECAPTCHA_THRESHOLD=0.5
    ports:
      - "8080:8080"
```

**docker run:**

```bash
docker run -d \
  -e RECAPTCHA_SECRET_KEY=your_secret_key \
  -e RECAPTCHA_SITE_KEY=your_site_key \
  -e RECAPTCHA_THRESHOLD=0.5 \
  -p 8080:8080 \
  your-image-name
```

### 6. Cloud Platforms

#### AWS Elastic Beanstalk

```bash
eb setenv RECAPTCHA_SECRET_KEY=your_secret_key \
          RECAPTCHA_SITE_KEY=your_site_key \
          RECAPTCHA_THRESHOLD=0.5
```

#### Heroku

```bash
heroku config:set RECAPTCHA_SECRET_KEY=your_secret_key
heroku config:set RECAPTCHA_SITE_KEY=your_site_key
heroku config:set RECAPTCHA_THRESHOLD=0.5
```

#### Google Cloud Run

```bash
gcloud run deploy --set-env-vars \
  "RECAPTCHA_SECRET_KEY=your_secret_key,RECAPTCHA_SITE_KEY=your_site_key,RECAPTCHA_THRESHOLD=0.5"
```

#### Azure App Service

```bash
az webapp config appsettings set \
  --resource-group myResourceGroup \
  --name myApp \
  --settings \
    RECAPTCHA_SECRET_KEY=your_secret_key \
    RECAPTCHA_SITE_KEY=your_site_key \
    RECAPTCHA_THRESHOLD=0.5
```

## Production Best Practices

### 1. Use Secret Management Services

**AWS Secrets Manager:**

```java
@Configuration
public class AwsSecretsConfig {
    @Bean
    public RecaptchaConfig recaptchaConfig() {
        // Fetch from AWS Secrets Manager
        String secretString = getSecretFromAWS("recaptcha-keys");
        // Parse and return config
    }
}
```

**Azure Key Vault:**

```properties
# application.properties
recaptcha.secret-key=${azure.keyvault.secrets.recaptcha-secret-key}
recaptcha.site-key=${azure.keyvault.secrets.recaptcha-site-key}
```

### 2. Separate Environments

```bash
# Development
RECAPTCHA_SECRET_KEY=dev_secret_key_here

# Staging
RECAPTCHA_SECRET_KEY=staging_secret_key_here

# Production
RECAPTCHA_SECRET_KEY=prod_secret_key_here
```

### 3. Key Rotation

1. Generate new keys in Google reCAPTCHA Console
2. Update environment variables
3. Restart application (zero-downtime deployment)
4. Verify functionality
5. Remove old keys from console

## Verification

### Check if Variables are Set

**Linux/Mac:**

```bash
echo $RECAPTCHA_SECRET_KEY
echo $RECAPTCHA_SITE_KEY
```

**Windows:**

```powershell
$env:RECAPTCHA_SECRET_KEY
$env:RECAPTCHA_SITE_KEY
```

### Test Application Startup

```bash
mvn spring-boot:run

# Look for errors like:
# "Binding to target org.springframework.boot.context.properties.bind.BindException:
#  Failed to bind properties under 'recaptcha'"
```

## Troubleshooting

### Variables Not Loading

**Problem:** Application starts but keys are empty

**Solutions:**

1. Verify variables are set: `echo $RECAPTCHA_SECRET_KEY`
2. Check spelling (case-sensitive)
3. Reload shell after setting variables
4. Check IDE run configuration

### Application Won't Start

**Problem:** Startup fails with binding exception

**Solution:**

```bash
# Check if variables are set
export RECAPTCHA_SECRET_KEY=test
export RECAPTCHA_SITE_KEY=test
mvn spring-boot:run
```

### Variables Work in Terminal but Not in IDE

**Problem:** `mvn spring-boot:run` works, but IDE doesn't

**Solution:**

- Configure environment variables in IDE run configuration
- Restart IDE after setting system variables
- Use .env file with IDE plugin (e.g., EnvFile for IntelliJ)

## Security Checklist

- [ ] .env file is in .gitignore
- [ ] No keys committed to version control
- [ ] Production uses secret management service
- [ ] Different keys for each environment
- [ ] Keys rotated periodically
- [ ] Access to keys is restricted
- [ ] Keys are not logged
- [ ] Keys are not exposed in error messages

## Migration from Hardcoded Keys

If you had hardcoded keys before:

1. **Get current keys from code**
   ```bash
   # Search for old keys in code
   grep -r "6Lf" src/
   ```

2. **Set as environment variables**
   ```bash
   export RECAPTCHA_SECRET_KEY=found_secret_key
   export RECAPTCHA_SITE_KEY=found_site_key
   ```

3. **Test application**
   ```bash
   mvn clean spring-boot:run
   ```

4. **Verify functionality**
    - Visit http://localhost:8080/create/form
    - Submit form
    - Check logs for validation

5. **Remove hardcoded keys from code**
    - Already done in this implementation!

## Additional Resources

- [Spring Boot External Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [12-Factor App: Config](https://12factor.net/config)
- [Google reCAPTCHA Admin Console](https://www.google.com/recaptcha/admin)
