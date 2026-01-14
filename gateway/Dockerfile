# Stage 1: Build the application
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw
# Run build (skipping tests for speed)
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the final runtime image
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy only the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Configuration via environment variables
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]