FROM eclipse-temurin:17-jdk-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/transaction-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application runs on
EXPOSE 9081

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]