# ===== Frontend Build Stage =====
FROM node:18-alpine AS frontend-build
WORKDIR /app/frontend

# NPM configuration for better reliability
RUN npm config set \
    registry=https://registry.npmjs.org/ \
    strict-ssl=false \
    fetch-retry-mintimeout=20000 \
    fetch-retry-maxtimeout=120000 \
    fetch-retries=5

# Install dependencies
COPY frontend/package*.json ./
RUN apk add --no-cache curl && \
    for i in {1..3}; do \
        echo "Attempt $i: Installing npm packages..." && \
        npm install --prefer-offline --no-audit --no-fund --network-timeout=120000 && break || \
        echo "Attempt $i failed, waiting..." && sleep 5; \
    done

# Copy configuration files
COPY frontend/tsconfig*.json \
     frontend/vite.config.ts \
     frontend/postcss.config.js \
     frontend/tailwind.config.js ./

# Copy source files
COPY frontend/src ./src
COPY frontend/public ./public
COPY frontend/index.html ./

# Build frontend
RUN echo "Building frontend in production mode..." && \
    npm run build -- --mode production && \
    echo "Build complete. Contents:" && \
    ls -la dist/

# ===== Backend Build Stage =====
FROM maven:3.9.6-eclipse-temurin-21-alpine AS backend-build
WORKDIR /app/backend

# Download dependencies first for better caching
COPY backend/pom.xml ./
RUN mvn dependency:go-offline

# Copy source and frontend build
COPY backend/src ./src/
RUN mkdir -p ./src/main/resources/static/
COPY --from=frontend-build /app/frontend/dist/ ./src/main/resources/static/

# Build backend
RUN mvn clean package -DskipTests

# ===== Final Stage =====
FROM eclipse-temurin:21-jre-alpine

# Set environment variables
ENV JAVA_OPTS="-Xms512m -Xmx1g -XX:+ExitOnOutOfMemoryError" \
    SERVER_PORT=8080

# Create non-root user and required directories
RUN addgroup -S spring && \
    adduser -S spring -G spring && \
    mkdir -p /cloudsql && \
    chown -R spring:spring /cloudsql

# Set up application
WORKDIR /app
COPY --from=backend-build /app/backend/target/*.jar app.jar
USER spring:spring

# Configure container
EXPOSE 8080
CMD ["java", "-jar", "/app/app.jar"] 