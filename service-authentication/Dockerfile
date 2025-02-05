FROM eclipse-temurin:23-jdk-alpine

RUN apk add --no-cache maven

ENV MAVEN_OPTS="-Dmaven.repo.local=/root/.m2/repository"

COPY pom.xml /app/bom/pom.xml
WORKDIR /app/bom
RUN mvn clean install -DskipTests && rm -rf /app/bom

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

COPY service-authentication/.mvn .mvn
COPY service-authentication/mvnw mvnw
COPY service-authentication/pom.xml pom.xml
COPY service-authentication/src /app/src

RUN mvn clean package install -DskipTests

CMD ["mvn", "spring-boot:run"]