FROM maven:3.8.6-amazoncorretto-17 AS build

WORKDIR /build/

COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src/
RUN mvn package -DskipTests

FROM openjdk:17-alpine
ARG JAR_FILE=/build/target/*.jar
COPY --from=build $JAR_FILE /opt/library-service/app.jar
ENTRYPOINT ["java", "-jar", "/opt/library-service/app.jar"]
