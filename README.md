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

### 4. Configure reCAPTCHA v3 Keys

Update the following files with your reCAPTCHA v3 keys:

**In `src/main/java/com/hendisantika/recaptcha3/service/RecaptchaService.java`:**

```java
private final String RECAPTCHA_SECRET = "your_secret_key_here";
```

**In `src/main/resources/templates/form.html`:**

Replace `YOUR_SITE_KEY` in two places:

```html
<!-- In the script tag -->
<script th:src="@{https://www.google.com/recaptcha/api.js?render=YOUR_SITE_KEY}"></script>

<!-- In the JavaScript constant -->
const SITE_KEY = 'YOUR_SITE_KEY';
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

- The reCAPTCHA Secret Key should be stored securely (consider using environment variables or a secrets management
  system in production)
- Never commit your actual reCAPTCHA keys to version control
- The current implementation stores the secret key in the source code for demonstration purposes only
- Adjust the score threshold based on your application's security needs and acceptable false positive rate
- Monitor reCAPTCHA scores in production to fine-tune your threshold
- Consider implementing different thresholds for different actions (e.g., stricter for payments, looser for comments)

## Recommended Production Enhancements

1. Move reCAPTCHA keys to environment variables or external configuration
2. Add input validation and error handling
3. Implement update and delete operations for employees
4. Add pagination for employee listing
5. Implement proper logging
6. Add unit and integration tests
7. Use HTTPS in production
8. Add authentication and authorization

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
