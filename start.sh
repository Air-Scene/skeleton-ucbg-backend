#!/bin/sh

echo "[CONTAINER_STARTUP] ========================================" >&2
echo "[CONTAINER_STARTUP] Container startup at $(date)" >&2
echo "[CONTAINER_STARTUP] System information:" >&2
uname -a >&2

# Print network information
echo "[CONTAINER_STARTUP] Network information:" >&2
netstat -tulpn >&2 || echo "netstat not available" >&2

echo "[CONTAINER_STARTUP] ========================================" >&2
echo "[CONTAINER_STARTUP] Configuration:" >&2
echo "[CONTAINER_STARTUP] PORT: ${PORT:-8080}" >&2
echo "[CONTAINER_STARTUP] SPRING_PROFILES_ACTIVE: ${SPRING_PROFILES_ACTIVE}" >&2
echo "[CONTAINER_STARTUP] ========================================" >&2

# Start Spring Boot application
echo "[CONTAINER_STARTUP] Starting Spring Boot application..." >&2
exec java ${JAVA_OPTS} \
     -Dserver.port=8080 \
     -Dmanagement.server.port=8080 \
     -Dserver.address=0.0.0.0 \
     -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} \
     -Dlogging.level.root=INFO \
     -Dlogging.level.org.springframework=DEBUG \
     -Dlogging.level.com.example=DEBUG \
     -Dlogging.level.com.zaxxer.hikari=DEBUG \
     -Dlogging.level.org.hibernate.SQL=DEBUG \
     -Dlogging.level.com.google.cloud.sql=DEBUG \
     -Dcom.google.cloud.sql.socket.factory.logging.enabled=true \
     -jar app.jar 