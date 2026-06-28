FROM maven:3.8.1-openjdk-17 AS build

WORKDIR /app

COPY  . .

RUN mvn clean install -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre-alpine

RUN mkdir -p /app

RUN mkdir -p /app/config

WORKDIR /app

RUN echo "" > /app/config/application.properties

COPY --from=build /app/target/contract-wisor-api-0.0.1-SNAPSHOT.jar /app/lib/contract-wisor-api.jar

ENV SPRING_CONFIG_LOCATION=/app/config/application.properties

ENTRYPOINT ["java", "-jar", "/app/lib/contract-wisor-api.jar", "--scheduler=true"]

EXPOSE 8081
