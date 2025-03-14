
FROM gradle:8.11.1-jdk17-alpine AS builder

WORKDIR /app

COPY . .

RUN gradle clean build -x test

FROM openjdk:17-jdk-alpine
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
