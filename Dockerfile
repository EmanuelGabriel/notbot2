## Etapa do build
FROM maven:3.9.9-eclipse-temurin-11-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

## Etapa de execução
FROM openjdk:11-jdk-slim
WORKDIR /app
COPY --from=build /app/target/notbot2-0.0.1-SNAPSHOT.jar notbot2-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "notbot2-0.0.1-SNAPSHOT.jar"]


