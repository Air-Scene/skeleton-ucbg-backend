# Build stage
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy pom.xml for dependency resolution
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine

# Create non-root user
RUN addgroup -S spring && \
    adduser -S spring -G spring

# Set working directory
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar
USER spring:spring

# Configure memory and port
ENV JAVA_OPTS="-Xms512m -Xmx1g -XX:+ExitOnOutOfMemoryError" \
    PORT=8080

# Expose port
EXPOSE ${PORT}

# Run with explicit port configuration
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar --server.port=${PORT}"] 