# Spring Boot Google reCAPTCHA v3

A Spring Boot application demonstrating Google reCAPTCHA v3 integration with employee management functionality.

## Features

- Employee management system (Create, Read operations)
- Google reCAPTCHA v3 integration for invisible bot protection
- Score-based validation (0.0-1.0 scale) to determine bot likelihood
- Action verification for enhanced security
- MySQL database integration with JPA/Hibernate
- Thymeleaf templates for UI
- Bootstrap 5 for responsive design
- Lombok for reducing boilerplate code

## Technologies

- **Java**: 21
- **Spring Boot**: 3.5.8
- **Database**: MySQL 8.0
- **ORM**: Hibernate/JPA
- **Template Engine**: Thymeleaf
- **Build Tool**: Maven
- **UI Framework**: Bootstrap 5.3.0

## Project Structure

```
src/
├── main/
│   ├── java/com/hendisantika/recaptcha3/
│   │   ├── SpringBootGoogleRecaptcha2Application.java  # Main application class
│   │   ├── controller/
│   │   │   └── EmployeeController.java                  # Employee CRUD operations
│   │   ├── dto/
│   │   │   └── EmployeeDTO.java                         # Data Transfer Object
│   │   ├── entity/
│   │   │   └── Employee.java                            # JPA Entity
│   │   ├── repository/
│   │   │   └── EmployeeRepository.java                  # JPA Repository
│   │   ├── response/
│   │   │   └── RecaptchaResponse.java                   # reCAPTCHA API response
│   │   └── service/
│   │       ├── EmployeeService.java                     # Business logic
│   │       └── RecaptchaService.java                    # reCAPTCHA validation
│   └── resources/
│       ├── application.properties                        # Application configuration
│       ├── banner.txt                                    # Custom Spring Boot banner
│       ├── import.sql                                    # Initial data
│       └── templates/                                    # Thymeleaf templates
│           ├── index.html                                # List employees
│           ├── form.html                                 # Create employee form
│           └── error.html                                # Error page
```

## Prerequisites

- JDK 21 or higher
- Maven 3.6+
- MySQL 8.0
- Google reCAPTCHA v3 API keys (Site Key and Secret Key)

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/hendisantika/spring-boot-google-recaptcha2.git
cd spring-boot-google-recaptcha2
```

### 2. Configure MySQL Database

Create a MySQL database or let the application create it automatically:

```sql
CREATE DATABASE capcay;
```

Update `src/main/resources/application.properties` with your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/capcay?createDatabaseIfNotExist=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Jakarta&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
```

### 3. Get Google reCAPTCHA v3 Keys

1. Go to [Google reCAPTCHA Admin Console](https://www.google.com/recaptcha/admin)
2. Register a new site with **reCAPTCHA v3** (NOT v2)
3. Add your domain(s) - for local development, add `localhost`
4. Copy your Site Key and Secret Key

**Important**: Make sure to select reCAPTCHA v3, not v2. v3 works invisibly without user interaction.

### 4. Configure reCAPTCHA v3 Keys via Environment Variables

The application uses environment variables for security. Set your reCAPTCHA keys as environment variables:

**Option A: Using .env file (Recommended for Development)**

1. Copy the example file:
   ```bash
   cp .env.example .env
   ```

2. Edit `.env` and add your keys:
   ```bash
   RECAPTCHA_SECRET_KEY=your_secret_key_here
   RECAPTCHA_SITE_KEY=your_site_key_here
   RECAPTCHA_THRESHOLD=0.5
   ```

3. Load environment variables before running (or use your IDE's environment configuration):
   ```bash
   # Linux/Mac
   export $(cat .env | xargs)

   # Or use direnv, dotenv, etc.
   ```

**Option B: Set Environment Variables Directly**

```bash
# Linux/Mac
export RECAPTCHA_SECRET_KEY=your_secret_key_here
export RECAPTCHA_SITE_KEY=your_site_key_here
export RECAPTCHA_THRESHOLD=0.5

# Windows (Command Prompt)
set RECAPTCHA_SECRET_KEY=your_secret_key_here
set RECAPTCHA_SITE_KEY=your_site_key_here
set RECAPTCHA_THRESHOLD=0.5

# Windows (PowerShell)
$env:RECAPTCHA_SECRET_KEY="your_secret_key_here"
$env:RECAPTCHA_SITE_KEY="your_site_key_here"
$env:RECAPTCHA_THRESHOLD="0.5"
```

**Option C: Pass as Command Line Arguments**

```bash
java -jar target/recaptcha2-0.0.1-SNAPSHOT.jar \
  --recaptcha.secret-key=your_secret_key_here \
  --recaptcha.site-key=your_site_key_here \
  --recaptcha.threshold=0.5
```

**Option D: IDE Configuration (IntelliJ IDEA)**

1. Go to Run → Edit Configurations
2. Select your Spring Boot configuration
3. Add to "Environment variables":
   ```
   RECAPTCHA_SECRET_KEY=your_secret_key_here;RECAPTCHA_SITE_KEY=your_site_key_here;RECAPTCHA_THRESHOLD=0.5
   ```

### 5. Build the Project

```bash
mvn clean package
```

### 6. Run the Application

```bash
java -jar target/recaptcha2-0.0.1-SNAPSHOT.jar
```

Or run directly with Maven:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Usage

### Access the Application

1. Open your browser and navigate to `http://localhost:8080`
2. Click "Create Employee" button
3. Fill in the employee details:
    - First Name
    - Last Name
    - Birthdate
4. Complete the reCAPTCHA challenge
5. Click "Submit"

### API Endpoints

- `GET /` or `GET /all` - List all employees
- `GET /create/form` - Display employee creation form
- `POST /create/process` - Process employee creation with reCAPTCHA validation

## Database Schema

### Employee Table

| Column        | Type         | Constraints                 |
|---------------|--------------|-----------------------------|
| id            | BIGINT       | PRIMARY KEY, AUTO_INCREMENT |
| name          | VARCHAR(255) |                             |
| last_name     | VARCHAR(255) |                             |
| date_of_birth | DATE         | NOT NULL                    |

## Configuration

### Environment Variables

The application requires the following environment variables:

| Variable               | Required | Default | Description                  |
|------------------------|----------|---------|------------------------------|
| `RECAPTCHA_SECRET_KEY` | Yes      | -       | Your reCAPTCHA v3 Secret Key |
| `RECAPTCHA_SITE_KEY`   | Yes      | -       | Your reCAPTCHA v3 Site Key   |
| `RECAPTCHA_THRESHOLD`  | No       | 0.5     | Score threshold (0.0-1.0)    |

### application.properties

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/capcay?createDatabaseIfNotExist=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Jakarta&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.generate-ddl=true
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.open-in-view=false

# Google reCAPTCHA v3 Configuration (reads from environment variables)
recaptcha.secret-key=${RECAPTCHA_SECRET_KEY:}
recaptcha.site-key=${RECAPTCHA_SITE_KEY:}
recaptcha.threshold=${RECAPTCHA_THRESHOLD:0.5}
```

## How reCAPTCHA v3 Works in This Application

reCAPTCHA v3 works completely invisibly in the background, analyzing user behavior to determine if they are a bot.

### Flow:

1. When a user visits the employee creation form, the reCAPTCHA v3 script loads invisibly
2. When the user clicks "Submit", JavaScript intercepts the form submission
3. `grecaptcha.execute()` is called with the action "submit" to generate a token
4. The token is automatically added to a hidden field and the form is submitted
5. On the server, `RecaptchaService` validates the token by making a POST request to Google's API
6. Google returns a response with:
    - `success`: boolean indicating if the token is valid
    - `score`: 0.0-1.0 (1.0 = very likely human, 0.0 = very likely bot)
    - `action`: the action name for verification
7. The server checks if:
    - The request was successful
    - The score is above the threshold (default: 0.5)
    - The action matches the expected action ("submit")
8. If validation passes, the employee is created; otherwise, an error page is displayed

### Score Interpretation:

- **0.9 - 1.0**: Very likely a human
- **0.5 - 0.9**: Probably a human
- **0.3 - 0.5**: Suspicious activity
- **0.0 - 0.3**: Very likely a bot

The default threshold is **0.5**, which you can adjust in `RecaptchaService.java`:

```java
private static final double SCORE_THRESHOLD = 0.5; // Adjust as needed
```

## reCAPTCHA v3 vs v2: Key Differences

| Feature              | v2                                         | v3                                        |
|----------------------|--------------------------------------------|-------------------------------------------|
| **User Interaction** | Requires checkbox click or image challenge | Completely invisible, no user interaction |
| **User Experience**  | Can interrupt user flow                    | Seamless, no interruption                 |
| **Validation**       | Binary (pass/fail)                         | Score-based (0.0 - 1.0)                   |
| **Bot Detection**    | Challenge-based                            | Behavioral analysis                       |
| **Action Tracking**  | Not available                              | Can track specific actions                |
| **Customization**    | Limited                                    | Adjustable score thresholds               |
| **False Positives**  | Lower (explicit challenges)                | Possible (requires threshold tuning)      |

## Security Notes

- **Environment Variables**: reCAPTCHA keys are now stored as environment variables, not in source code
- **Never commit keys**: The `.env` file is gitignored - never commit actual keys to version control
- **Production**: Use your cloud provider's secret management (AWS Secrets Manager, Azure Key Vault, etc.)
- **Threshold Tuning**: Adjust the score threshold based on your security needs and acceptable false positive rate
- **Monitoring**: Monitor reCAPTCHA scores in production to fine-tune your threshold
- **Action-Based**: Consider implementing different thresholds for different actions (e.g., stricter for payments,
  looser for comments)
- **Key Rotation**: Rotate your reCAPTCHA keys periodically for enhanced security

## Recommended Production Enhancements

1. ✅ ~~Move reCAPTCHA keys to environment variables~~ (Already implemented!)
2. Use cloud-based secret management (AWS Secrets Manager, Azure Key Vault, GCP Secret Manager)
3. Add input validation and error handling
4. Implement update and delete operations for employees
5. Add pagination for employee listing
6. Implement proper logging and monitoring
7. Add unit and integration tests
8. Use HTTPS in production
9. Add authentication and authorization
10. Implement rate limiting
11. Add health checks and metrics endpoints

## Troubleshooting

### Application won't start

- Ensure MySQL is running
- Verify database credentials in `application.properties`
- Check if port 8080 is available

### reCAPTCHA validation fails

- Verify your Secret Key is correct in `RecaptchaService.java`
- Ensure your domain is registered in Google reCAPTCHA console (including `localhost` for development)
- Check that the Site Key in `form.html` matches your reCAPTCHA v3 configuration (replace both instances of
  `YOUR_SITE_KEY`)
- Make sure you registered for **v3**, not v2 - they use different keys
- Check browser console for JavaScript errors
- Verify the action name matches between frontend ("submit") and backend validation
- Check application logs for the score value to see if it's below the threshold

### Lombok compilation errors

- Ensure your IDE has Lombok plugin installed
- The project is configured with Lombok annotation processor in the Maven plugin

## Author

**Hendi Santika**

- Email: hendisantika@gmail.com
- Telegram: @hendisantika34
- GitHub: [@hendisantika](https://github.com/hendisantika)

## License

This project is for educational and demonstration purposes.

## Contributing

Feel free to submit issues and enhancement requests!

## Acknowledgments

- Spring Boot Team
- Google reCAPTCHA
- Bootstrap Team
