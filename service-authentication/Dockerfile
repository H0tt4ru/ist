FROM maven:3.8.7-openjdk-23 AS build

WORKDIR /app

COPY bom /app/bom

RUN mvn -f /app/bom/pom.xml clean ins