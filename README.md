# Spring Boot and React Full-Stack Application 🚀

A modern full-stack application featuring Spring Boot backend, React frontend, and PostgreSQL database. Complete with user authentication, profile management, and admin features.

## 📋 Features

- User Authentication & Authorization
- User Profile Management
- Customer Profile Management
- Admin Dashboard
- Password Reset Functionality
- Email Notifications (MailHog locally, Mailtrap in cloud dev, SMTP in production)

## 🛠️ Tech Stack

- **Backend:** Spring Boot (Java 21)
- **Frontend:** React with TypeScript
- **Database:** PostgreSQL
- **Email Services:** 
  - Local: MailHog for development
  - Cloud Dev: Mailtrap for testing
  - Production: SMTP Provider
- **Containerization:** Docker

## 🚀 Getting Started

### Prerequisites

- Java 21 or higher
- Node.js (v16 or higher)
- Docker and Docker Compose
- Maven
- npm or yarn

### Environment Setup

1. Clone the repository
2. Set up environment files:
   ```bash
   # For development
   cp .env.example .env

   # For production
   cp .env.prod.example .env.prod
   ```
3. Update the environment files with your values

> ⚠️ Never commit `.env` or `.env.prod` files as they contain sensitive information.

## 💻 Development (Local) Setup

### Option 1: Full Docker Setup
```bash
# Start all services
docker-compose up

# Run frontend separately
cd frontend && npm run dev
```

### Option 2: Individual Services

#### Database
```bash
cd database
docker-compose up -d
```

#### MailHog (Email Testing)
```bash
cd mail
docker-compose up -d
# Access MailHog UI at http://localhost:8025
```

#### Backend
```bash
cd backend
mvn spring-boot:run
# Or run DemoApplication from your IDE
```

#### Frontend
```bash
cd frontend
npm install
npm run dev
# Access frontend at http://localhost:5173
```

## 🌐 Production Docker Images

### Using Docker Compose
```bash
docker-compose --env-file .env.prod up
```

### Individual Services
```bash
# Database
cd database
docker-compose --env-file .env.prod up -d

# Backend
cd backend
docker-compose --env-file .env.prod up -d

# Frontend
cd frontend
npm run build
```

## 📧 Email Configuration

The application uses different email services for each environment:

### Local Development
- **Service**: MailHog (included in docker-compose)
- **Setup**: `cd mail && docker-compose up -d`
- **UI Access**: `http://localhost:8025`
- **Configuration**:
  ```yaml
  MAIL_HOST=mailhog
  MAIL_PORT=1025
  # No authentication needed
  ```

### Cloud Development
- **Service**: Mailtrap
- **Purpose**: Testing & debugging without sending real emails
- **Configuration** (in GitHub Actions):
  ```yaml
  MAIL_HOST=smtp.mailtrap.io
  MAIL_PORT=2525
  MAIL_USERNAME=your_username  # From Mailtrap dashboard
  MAIL_PASSWORD=your_password  # From Mailtrap dashboard
  ```

### Production
- **Service**: SMTP Provider (e.g., SendGrid, Amazon SES)
- **Purpose**: Real email delivery
- **Configuration**:
  ```yaml
  MAIL_HOST=your.smtp.provider
  MAIL_PORT=587  # or as specified by provider
  MAIL_USERNAME=your_username
  MAIL_PASSWORD=your_password
  ```

## 🔄 Deployment & Environments

### Local Development
- **Purpose**: Development & testing on your machine
- **Services**: All run via Docker Compose
  - PostgreSQL database
  - MailHog for emails
  - Backend (Spring Boot)
  - Frontend (React dev server)
- **Debug**: All debug modes & hot reloading enabled

### Cloud Development (Google Cloud Run)
- **Purpose**: Team testing & integration
- **Service Names**: Prefixed with 'dev-'
  - `dev-react-springboot`: Main application
- **Resources**:
  - CPU: 1 core
  - Memory: 2GB
  - Instances: 0-2 (scale to zero for cost saving)
- **Configuration**:
  - Debug modes enabled
  - SQL logging enabled
  - Mailtrap for email testing
  - Development-specific security settings

### Production (Google Cloud Run)
- **Purpose**: Live application
- **Service Names**: No prefix
  - `react-springboot`: Main application
- **Resources**:
  - CPU: 2 cores
  - Memory: 4GB
  - Instances: 1-10 (always available)
- **Configuration**:
  - All debug modes disabled
  - Minimal logging
  - Production SMTP provider
  - Maximum security settings

### Deployment Process
1. Development:
   ```bash
   # Work in development branch
   git checkout development
   # Make changes and commit
   git add .
   git commit -m "Your changes"
   git push origin development
   # Deploy using GitHub Actions:
   # 1. Select "Deploy to Development Environment"
   # 2. Use workflow from: main
   # 3. Run workflow on: development
   ```

2. Merging to Production:
   ```bash
   # First, update development with any changes from main
   git checkout development
   git merge main
   
   # Test in development environment
   # (Run development deployment and verify everything works)
   
   # Once tested, merge to main
   git checkout main
   git merge development
   git push origin main
   
   # Deploy using GitHub Actions:
   # 1. Changes will auto-deploy (on push to main)
   # OR
   # 1. Select "Deploy to Cloud Run"
   # 2. Use workflow from: main
   # 3. Run workflow on: main
   ```

## 📁 Project Structure
```
.
├── backend/         # Spring Boot application
├── frontend/        # React application
├── database/        # Database setup and migrations
└── mail/           # MailHog for local development
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 🔄 Branch Management & Deployments

### Branch Structure
- `main`: Production branch containing both workflow files
- `development`: Development branch for feature development

### Workflow Files (both in main branch)
- `.github/workflows/deploy-to-cloud-run.yml` - Production deployment
- `.github/workflows/deploy-to-cloud-run-dev.yml` - Development deployment

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.