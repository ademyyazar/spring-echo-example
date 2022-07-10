FROM maven:3.8.5-openjdk-17-slim AS builder
ADD ./pom.xml pom.xml
ADD ./src src
RUN mvn clean package

FROM eclipse-temurin:17.0.3_7-jdk-alpine
COPY --from=builder target/spring-echo-example-1.1.0.jar dream-app-1.0.0.jar
CMD["java", "-jar", "dream-app-1.0.0.jar"]