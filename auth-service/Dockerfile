# Stage 1: Build the application
FROM bellsoft/liberica-openjdk-alpine-musl:17 AS build
RUN apk add --no-cache maven
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: final image
FROM bellsoft/liberica-openjdk-alpine-musl:17
WORKDIR /app
COPY --from=build /app/target/auth-service-0.0.1-SNAPSHOT.jar auth-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "auth-service.jar"]
