FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copy the built JAR file into the container
COPY target/XposeAPI.jar app.jar

# Expose the port your app runs on (default 8080)
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
