# Spring Boot Backend Application 🚀

A modern Spring Boot backend application with PostgreSQL database, featuring user authentication, profile management, and admin features.

## 📋 Features

- User Authentication & Authorization (JWT)
- User Profile Management
- Admin Dashboard
- Email Notifications
- RESTful API
- PostgreSQL Database
- Railway.app Deployment

## 🛠️ Tech Stack

- **Backend:** Spring Boot 3.2 (Java 21)
- **Database:** PostgreSQL
- **Security:** JWT, Spring Security
- **Deployment:** Railway.app

## 🚀 Getting Started

### Prerequisites

- Java 21 or higher
- Maven
- Docker (for local development)
- PostgreSQL (or Docker)

### Development Environment

The project includes Docker configurations for local development:

- **`/database`**: PostgreSQL setup for local development
  ```bash
  cd database
  cp .env.example .env  # Configure your database variables
  docker-compose up -d
  ```

- **`/mailhog`**: Local email testing server
  ```bash
  cd mailhog
  docker-compose up -d
  # Access mailhog UI at http://localhost:8025
  ```

### Local Development Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd backend
   ```

2. **Set up the development environment**
   ```bash
   # Start PostgreSQL
   cd database
   docker-compose up -d

   # Start MailHog
   cd ../mailhog
   docker-compose up -d
   ```

3. **Run the application**
   ```bash
   # Using Maven
   mvn spring-boot:run

   # Or using your IDE
   # Run DemoApplication.java
   ```

4. **Access the application**
   - API: http://localhost:8080
   - MailHog UI: http://localhost:8025 (for email testing)

## 🌐 Production Deployment

### Railway Deployment

1. **Create a Railway Account**
   - Sign up at [Railway.app](https://railway.app)
   - Install Railway CLI: `curl -fsSL https://railway.app/install.sh | sh`

2. **Set up Railway Project**
   ```bash
   # Login to Railway
   railway login

   # Create new project
   railway init

   # Add PostgreSQL
   railway add postgresql
   ```

3. **Configure Environment Variables**
   Required variables in Railway dashboard:
   ```properties
   # Required Secrets
   JWT_SECRET=your-jwt-secret-key
   MAIL_USERNAME=your-smtp-username
   MAIL_PASSWORD=your-smtp-password

   # Configuration
   SPRING_PROFILES_ACTIVE=prod
   MAIL_HOST=smtp.gmail.com
   MAIL_PORT=587
   ```

4. **Deploy**
   ```bash
   # Manual deployment
   railway up

   # Or use GitHub Actions (recommended)
   git push origin main
   ```

## 📝 API Documentation

### Main Endpoints

- **Authentication**
  - POST `/api/v1/auth/login`
  - POST `/api/v1/auth/register`
  
- **User Management**
  - GET `/api/v1/accounts/me`
  - PUT `/api/v1/accounts/me`
  
- **Admin Operations**
  - GET `/api/v1/accounts` (Admin only)
  - GET `/api/v1/accounts/{id}` (Admin only)

Full API documentation available at `/swagger-ui.html` when running locally.

## 🔧 Configuration

### Application Properties
Key configurations in `application.yml`:
```yaml
spring:
  datasource:
    url: ${DATABASE_URL:jdbc:postgresql://localhost:5432/myapp}
  jpa:
    hibernate:
      ddl-auto: ${JPA_DDL_AUTO:update}
```

### Environment Profiles
- **Local**: Default profile for development
- **Prod**: Optimized for Railway deployment

## 🔍 Troubleshooting

Common issues and solutions:

1. **Database Connection Issues**
   - Check DATABASE_URL environment variable
   - Verify PostgreSQL is running
   - Check network/firewall settings

2. **Email Sending Fails**
   - Verify SMTP credentials
   - Check email service configuration
   - Review email logs

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Spring Boot Team
- Railway.app Team
- PostgreSQL Team

## 🔐 Environment Setup

1. Create GitHub environments: `development` and `production`
2. Add required secrets to each environment:
   - `RAILWAY_TOKEN`
   - `RAILWAY_PROJECT_ID`
   - `JWT_SECRET`
   - `MAIL_USERNAME`
   - `MAIL_PASSWORD`

See `.github/env/.env.example` for all required variables.