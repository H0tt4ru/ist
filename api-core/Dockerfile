FROM eclipse-temurin:23-jdk AS build

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:23-jre

WORKDIR /app

COPY --from=build /app/target/api-core.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]