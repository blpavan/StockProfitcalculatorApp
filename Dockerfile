#LABEL authors="lakshmanpavankumarbodapati"

# Step 1: Build the application
FROM maven:3.9.1 AS Builder

# Set the working directory in the container
WORKDIR /StockProfitCalculator

# Copy the pom.xml file
COPY pom.xml .

# Download the dependencies
RUN mvn dependency:go-offline

# Copy the source code into the container
COPY src ./src

# Package the application, skipping tests
RUN mvn package -DskipTests

# Step 2: Create the runtime environment
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /StockProfitCalculator

# Copy the JAR file from the Builder stage
COPY --from=Builder /StockProfitCalculator/target/StockProfitCalculator-0.0.1-SNAPSHOT.jar StockProfitCalculator.jar

# Expose the application on port 8080
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "StockProfitCalculator.jar"]
