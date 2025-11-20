# Spring Boot Google reCAPTCHA v2

A Spring Boot application demonstrating Google reCAPTCHA v2 integration with employee management functionality.

## Features

- Employee management system (Create, Read operations)
- Google reCAPTCHA v2 integration for form protection
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
- Google reCAPTCHA v2 API keys (Site Key and Secret Key)

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

### 3. Get Google reCAPTCHA Keys

1. Go to [Google reCAPTCHA Admin Console](https://www.google.com/recaptcha/admin)
2. Register a new site with reCAPTCHA v2 (Checkbox)
3. Copy your Site Key and Secret Key

### 4. Configure reCAPTCHA Keys

Update the following files with your reCAPTCHA keys:

**In `src/main/java/com/hendisantika/recaptcha3/service/RecaptchaService.java`:**

```java
private final String RECAPTCHA_SECRET = "your_secret_key_here";
```

**In `src/main/resources/templates/form.html`:**

```html
<div class="g-recaptcha" data-sitekey="your_site_key_here"></div>
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

## How reCAPTCHA Works in This Application

1. When a user visits the employee creation form, the reCAPTCHA widget is loaded from Google
2. The user must complete the CAPTCHA challenge before submitting the form
3. Upon form submission, the CAPTCHA response token is sent to the server
4. The `RecaptchaService` validates the token by making a POST request to Google's reCAPTCHA API
5. If validation succeeds, the employee is created; otherwise, an error page is displayed

## Security Notes

- The reCAPTCHA Secret Key should be stored securely (consider using environment variables or a secrets management
  system in production)
- Never commit your actual reCAPTCHA keys to version control
- The current implementation stores the secret key in the source code for demonstration purposes only

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

- Verify your Secret Key is correct
- Ensure your domain is registered in Google reCAPTCHA console
- Check that the Site Key in `form.html` matches your reCAPTCHA configuration

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
