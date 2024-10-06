package com.stockprice.service;

import com.opencsv.exceptions.CsvValidationException;
import com.stockprice.models.StockPriceResourceObject;
import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;


@Service
public class StockProfitCalculatorService {

    private static final Logger logger = LoggerFactory.getLogger(StockProfitCalculatorService.class);
    private final ResourceLoader resourceLoader;

    public StockProfitCalculatorService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Calculates the stock profit based on the provided stock Name and year.
     *
     * @param stockName the symbol of the stock to calculate profit for
     * @param year        the year for which the profit is to be calculated
     * @return a StockPriceResourceObject containing the calculated stock price details for the given stockName and Year
     * @throws IllegalArgumentException if the stock Name or year is invalid
     */
    public StockPriceResourceObject maxProfit(String stockName, int year) throws FileNotFoundException {

        String csvFileName = stockName.toUpperCase() + ".csv";
        Resource resource = null;
        try {
            resource = resourceLoader.getResource(csvFileName);
            if(!resource.exists()) {
                throw new Exception("Resource cannot be null: " + csvFileName);
            }
        } catch (Exception e) {
            throw new FileNotFoundException("File not found: " + csvFileName);
        }

        StockPriceResourceObject stockPriceResourceObject = new StockPriceResourceObject();

        double minPrice = Double.MAX_VALUE;
        String buyDate = "";
        String sellDate = "";
        double maxProfit = 0;


        try (InputStream inputStream = resource.getInputStream();
             CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {

            String[] nextLine;

            if (reader.readNext() == null) {
                throw new IllegalArgumentException("CSV file is empty or missing header");
            }

            while ((nextLine = reader.readNext()) != null) {
                String dateStr = nextLine[0];
                double openPrice = Double.parseDouble(nextLine[1]);
                double closingPrice = Double.parseDouble(nextLine[4]);

                int currentYear = Integer.parseInt(dateStr.split("-")[0]);

                if(currentYear == year) {
                    if (openPrice < minPrice) {
                        minPrice = openPrice;
                        buyDate = dateStr;
                    }

                    double potentialProfit = closingPrice - minPrice;
                    if (potentialProfit > maxProfit) {
                        maxProfit = potentialProfit;
                        sellDate = dateStr;
                    }
                }
            }
            if(maxProfit > 0) {
                stockPriceResourceObject.setBuyDate(LocalDate.parse(buyDate));
                stockPriceResourceObject.setBuyPrice(minPrice);
                stockPriceResourceObject.setSellDate(LocalDate.parse(sellDate));
                stockPriceResourceObject.setSellPrice(minPrice + maxProfit);
                stockPriceResourceObject.setProfit(maxProfit);
            }
            else {
                logger.info("No profit for {} in {}", stockName, year);
                stockPriceResourceObject.setProfit(0.0);
            }
        }
        catch (IOException | CsvValidationException e) {
            logger.error("Error processing CSV file", e);
        }
        catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid number format in CSV file");
        }
    return stockPriceResourceObject;
    }
}