# Use the official OpenJDK 17 base image
FROM openjdk:17-jdk-slim

# Define the argument for the JAR file
ARG JAR_FILE=target/*.jar

# Copy the built JAR file into the image
COPY ${JAR_FILE} app.jar

# Set the command to run your Spring Boot application
ENTRYPOINT ["java", "-jar", "/app.jar"]