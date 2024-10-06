    Stock Profit Calculator Application

Overview:
This is a command-line Java application that calculates the maximum profit for a company's stock in a given year based on historical stock price data from CSV files.

Prerequisites (Ensure the following software is installed on your machine):
Java JDK 17 or higher
Apache Maven for building the project
SpringBoot 3.3.4
An IDE (e.g., IntelliJ IDEA, Eclipse)
Git (to clone this repo)
Docker -- optional (to directly pull this docker image and run this application)

Third-Party Dependencies:
OpenCSV - 5.7.1 (To read and parse CSV files)
SLF4J: For logging purposes.
Spring Boot Starter: (Provides core features for building Spring applications)
Lombok: (A library that helps reduce boilerplate code by generating getters, setters, and other utility methods at compile time)
JUnit: 4.13.2  (For unit testing)


Project Structure (The key components of the project):

StockProfitCalculatorService: Contains the logic for calculating the maximum profit based on CSV data.
StockPriceResourceObject: Holds stock-related information such as buy date, sell date, buy price, sell price, and profit.
StockProfitCalculatorServiceTest: Contains unit tests for the application.
CSV file format: The stock data should be in CSV format with the following columns: Date,Open,High,Low,Close,Volume.

How to Build and Run:

Step 1: Clone the Repository
        git clone <repository-url>
        cd <repository-directory>

Step 2: Building the Project
        mvn clean install

Step 3: To Run the application
        mvn exec:java -Dexec.mainClass="com.stockprice.StockProfitCalculatorApplication" -Dexec.args="Arg1 Agr2"

        For Example:
        mvn exec:java -Dexec.mainClass="com.stockprice.StockProfitCalculatorApplication" -Dexec.args="AAPL 2023"

Step 4: Running Unit Tests
        mvn test

Running with Docker:
If you prefer not to clone this repository and manage dependencies yourself, you can run the application using Docker.

Follow these steps:

Step 1: Install Docker on Your Machine
        Download and install Docker from Docker's official website.

Step 2: Pull the Docker Image
     To pull the Docker image for the StockProfitCalculator app from Docker Hub, run the following command:

     Command: docker pull lbodapati1/stockprofitcalculator:latest

Step 3: Run the Docker Container
    After pulling the image, you can run the application using the following command:

    Command: docker run --rm lbodapati1/stockprofitcalculator:latest Arg1 Arg2

    For Example: docker run --rm lbodapati1/stockprofitcalculator:latest  AAPL 2024





