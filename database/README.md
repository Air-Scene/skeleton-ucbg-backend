# PostgreSQL Database Setup

## Overview
Spring Boot application database setup with:
- Dedicated user with limited permissions
- Separate `app` schema
- Automated table management (Hibernate/JPA)
- Docker-based development environment

## Directory Structure 
```
database/
├── config/postgresql.conf     # PostgreSQL configuration
├── init/01-init.sql          # Database initialization
├── .env                      # Environment variables
├── docker-compose.yml        # Docker compose config
└── Dockerfile               # Docker image config
```

## Quick Start
```bash
# Start/Stop
docker-compose up -d
docker-compose down

# Reset completely
docker-compose down -v && rm -rf data/postgres/ logs/postgres/ && docker-compose up -d
```

# Run database in production mode
cd database
```bash
docker-compose --env-file .env.prod up
```

## Configuration

### Environment Variables (.env)
```bash
# Required
POSTGRES_USER=app_user                  # Database user
POSTGRES_PASSWORD=your_secure_password  # Database password
POSTGRES_DB=myapp                      # Database name
POSTGRES_PORT=5432                     # Port (default)
POSTGRES_HOST=localhost                # Host (optional)
```

### Database Details
- **Connection**: localhost:5432/myapp
- **Schema**: app
- **Credentials**: app_user/your_secure_password (change in production!)

### Schema Management
1. **Initial Setup** (init/01-init.sql):
   - Creates user, schema, and permissions
   - Configures UUID extension

2. **Table Management**:
   - Automatic via Hibernate/JPA
   - Configuration: `spring.jpa.hibernate.ddl-auto=update`

## Development

### Database Access
```bash
# Access psql
docker exec -it postgres_db psql -U app_user -d myapp

# Useful psql commands
\dt app.*     # List tables
\d app.users  # Describe table
\dn          # List schemas
\du          # List users
```

### Docker Commands
```bash
docker-compose ps         # Status
docker-compose logs      # Logs
docker-compose restart   # Restart
```

## Maintenance & Backup

### Analysis & Backup
```bash
# Analysis (in psql)
ANALYZE VERBOSE;
SELECT pg_size_pretty(pg_total_relation_size('app.users'));

# Backup/Restore
docker exec -t postgres_db pg_dumpall -c -U app_user > backup.sql
cat backup.sql | docker exec -i postgres_db psql -U app_user -d myapp

# Automated daily backup
0 0 * * * docker exec -t postgres_db pg_dumpall -c -U app_user > backup_$(date +\%Y\%m\%d).sql
```

## Troubleshooting

### Common Issues & Solutions
1. **Container Issues**
   ```bash
   docker-compose logs postgres   # Check logs
   lsof -i :5432                # Check port conflicts
   sudo chown -R 999:999 data/postgres  # Fix permissions
   ```

2. **Schema Issues**
   ```sql
   -- Verify schema and permissions
   SELECT schema_name FROM information_schema.schemata;
   SELECT grantee, privilege_type 
   FROM information_schema.role_table_grants 
   WHERE table_schema = 'app';
   ```

### Security Checklist
- [ ] Change default credentials in production
- [ ] Use environment variables for sensitive data
- [ ] Update passwords regularly
- [ ] Keep PostgreSQL version updated

## Local Development
The database will be initialized with the scripts in `init/local/` directory.

## Cloud Deployment
For cloud deployment, you need to:

1. Copy `init/cloud/cloud-init.sql.example` to `init/cloud/cloud-init.sql`
2. Replace the placeholder values:
   - `your_database_name`: Your Cloud SQL database name
   - `your_database_user`: Your Cloud SQL user

Example:
```sql
GRANT CONNECT ON DATABASE my_app_db TO my_app_user;
```

Note: The `cloud-init.sql` file is gitignored for security. Make sure to keep your database credentials secure and never commit them to version control.
