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
# Add this line to install curl
RUN apk add --no-cache curl
COPY --from=build /app/target/stock-service-0.0.1-SNAPSHOT.jar stock-service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "stock-service.jar"]
