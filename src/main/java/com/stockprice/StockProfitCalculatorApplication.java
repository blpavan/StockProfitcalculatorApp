package com.stockprice;

import com.stockprice.models.StockPriceResourceObject;
import com.stockprice.service.StockProfitCalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockProfitCalculatorApplication implements CommandLineRunner {

	private final StockProfitCalculatorService stockProfitCalculatorService;
	private static final Logger logger = LoggerFactory.getLogger(StockProfitCalculatorApplication.class);

	public StockProfitCalculatorApplication(StockProfitCalculatorService stockProfitCalculatorService) {
		this.stockProfitCalculatorService = stockProfitCalculatorService;
	}

	public static void main(String[] args) {
		SpringApplication.run(StockProfitCalculatorApplication.class, args);
	}

	/**
	 * Runs the application logic after the Spring context is loaded.
	 *
	 * @param args command-line arguments that can be used to pass stock Name and year
	 */
	@Override
	public void run(String... args) {

		if (args.length != 2) {
			throw new IllegalArgumentException("Error: Please provide exactly 2 arguments - stock name and year");
        }

		String stockName = args[0];
		int year;

		if (stockName == null || stockName.isEmpty()) {
			throw new IllegalArgumentException("Error: Stock name cannot be null or empty");
		}

		try{
			year = Integer.parseInt(args[1]);
			if(year < 1900 || year > 2100){
				throw new IllegalArgumentException("Error: Year must be between 1900 and 2100");
			}
		}
		catch(NumberFormatException e){
			throw new IllegalArgumentException("Error: Invalid Year");
		}

        //logger.info("Provided Input Arguments are stock name: {}, Provided year: {}", stockName, year);
		System.out.println("Provided Input Arguments are stock name: " + stockName + ", Provided year: " + year);
		try {
			StockPriceResourceObject resourceObject = stockProfitCalculatorService.maxProfit(stockName, year);
			printResultFields(resourceObject);
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to calculate stock profit: " + e.getMessage(), e);
		}
	}

	/**
	 * Prints non-null fields for the result StockPriceResourceObject.
	 *
	 * @param stockPriceResourceObject the object containing stock price data
	 */
	private static void printResultFields(StockPriceResourceObject stockPriceResourceObject) {
		if (stockPriceResourceObject == null) {
			logger.info("No stock price data available.");
			return;
		}

		//logger.info("Stock Price Details:");
		System.out.println("Stock Price Details:");

		if (stockPriceResourceObject.getBuyDate() != null) {
			//logger.info("Buy Date: {}", stockPriceResourceObject.getBuyDate());
			System.out.println("Buy Date: " + stockPriceResourceObject.getBuyDate());
		}
		if (stockPriceResourceObject.getBuyPrice() != null) {
			//logger.info("Buy Price: {}", stockPriceResourceObject.getBuyPrice());
			System.out.println("Buy Price: " + stockPriceResourceObject.getBuyPrice());
		}
		if (stockPriceResourceObject.getSellDate() != null) {
			//logger.info("Sell Date: {}", stockPriceResourceObject.getSellDate());
			System.out.println("Sell Date: " + stockPriceResourceObject.getSellDate());
		}
		if (stockPriceResourceObject.getSellPrice() != null) {
			//logger.info("Sell Price: {}", stockPriceResourceObject.getSellPrice());
			System.out.println("Sell Price: " + stockPriceResourceObject.getSellPrice());
		}
		if (stockPriceResourceObject.getProfit() != null) {
			//logger.info("Profit: {}", stockPriceResourceObject.getProfit());
			System.out.println("Profit: " + stockPriceResourceObject.getProfit());
		}
	}
}
