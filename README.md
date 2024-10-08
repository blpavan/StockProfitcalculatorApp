Stock Profit Calculator Application

Author: Lakshman Pavan Kumar Bodapati

SourceCode (git repo) -> https://github.com/blpavan/StockProfitcalculatorApp

I. Overview:

This command-line Java application calculates the maximum profit for a company's stock in a given year based on historical stock price data from CSV files.

II. Prerequisites (Ensure the following software is installed on your machine):

        1. Java JDK 17 or higher

        2. Apache Maven for building the project

        3. SpringBoot 3.3.4

        4. An IDE (e.g., IntelliJ IDEA, Eclipse)

        5. Git (to clone this repo)

        6. Docker -- optional (to directly pull this docker image and run this application)

III. Third-Party Dependencies:

        1. OpenCSV - 5.7.1 (To read and parse CSV files)

        2. SLF4J: For logging purposes

        3. Spring Boot Starter: (Provides core features for building Spring applications)

        4. Lombok: (A library that helps reduce boilerplate code by generating getters, setters, and other utility methods at compile time)

        5. JUnit: 4.13.2  (For unit testing)


IV. Project Structure (The key components of the project):

        1. StockProfitCalculatorService: Contains the logic for calculating the maximum profit based on CSV data.

        2. StockPriceResourceObject: Holds stock-related information such as buy date, sell date, buy price, sell price, and profit.

        3. StockProfitCalculatorServiceTest: Contains unit tests for the application.

        4. CSV file format: The stock data should be in CSV format with the following columns: Date, Open, High, Low, Close, Volume.


V. How to Build and Run:

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

VI. Running with Docker:
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





