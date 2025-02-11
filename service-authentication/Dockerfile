FROM maven:3.9.9-eclipse-temurin-21-jammy AS builder

WORKDIR /app

# Copy BOM first (since it has no source code, only POM)
COPY pom.xml bom/pom.xml
WORKDIR /app/bom
RUN mvn clean install -DskipTests && rm -rf /app/bom

# Copy Base-Domain (has source code)
COPY base-domain /app/base-domain
WORKDIR /app/base-domain
RUN mvn clean install -DskipTests && rm -rf /app/base-domain

# Copy Shared-Utils (has source code)
COPY shared-utils /app/shared-utils
WORKDIR /app/shared-utils
RUN mvn clean install -DskipTests && rm -rf /app/shared-utils

# Copy API-Core (has source code)
COPY api-core /app/api-core
WORKDIR /app/api-core
RUN mvn clean install -DskipTests && rm -rf /app/api-core

# ---- Stage 2: Build Service ----
WORKDIR /app/service-authentication
COPY service-authentication /app/service-authentication
RUN mvn clean package -DskipTests

# ---- Stage 3: Runtime ----
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy only the built JAR from the builder stage
COPY --from=builder /app/service-authentication/target/*.jar app.jar

# Expose port 8080 for the service
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "app.jar"]