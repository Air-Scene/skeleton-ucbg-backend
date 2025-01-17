# Use OpenJDK 21 as the base image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY target/*.jar app.jar

# Set environment variable with a default value
ENV PORT=8080

# Expose the port the app runs on
EXPOSE ${PORT}

# Command to run the application with explicit port configuration
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT}"] 