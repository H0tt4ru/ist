# 🔥 Stage 1: Build Dependencies (BOM, base-domain, shared-utils, api-core)
FROM eclipse-temurin:23-jdk-alpine

RUN apk add --no-cache maven

# Persist Maven repository
ENV MAVEN_OPTS="-Dmaven.repo.local=/root/.m2/repository"

# Copy the parent BOM and install
COPY pom.xml /app/bom/pom.xml
WORKDIR /app/bom
RUN mvn clean install -DskipTests && rm -rf /app/bom

# Copy and install other dependencies
COPY base-domain /app/base-domain
WORKDIR /app/base-domain
RUN mvn clean install -DskipTests && rm -rf /app/base-domain

COPY shared-utils /app/shared-utils
WORKDIR /app/shared-utils
RUN mvn clean install -DskipTests && rm -rf /app/shared-utils

COPY api-core /app/api-core
WORKDIR /app/api-core
RUN mvn clean install -DskipTests && rm -rf /app/api-core

WORKDIR /app

COPY service-wallet/.mvn .mvn
COPY service-wallet/mvnw mvnw
COPY service-wallet/pom.xml pom.xml
COPY service-wallet/src /app/src

RUN mvn clean package -DskipTests

CMD ["mvn", "spring-boot:run"]