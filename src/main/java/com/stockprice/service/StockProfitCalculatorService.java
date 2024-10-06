package com.stockprice.service;

import com.opencsv.exceptions.CsvValidationException;
import com.stockprice.models.StockPriceResourceObject;
import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
     * @param year      the year for which the profit is to be calculated
     * @return a StockPriceResourceObject containing the calculated stock price details for the given stockName and Year
     * @throws FileNotFoundException if the CSV file for the stock cannot be found
     */
    public StockPriceResourceObject maxProfit(String stockName, int year) throws FileNotFoundException {

        String csvFileName = stockName.toUpperCase() + ".csv";
        Resource resource;
        try {
            resource = resourceLoader.getResource(csvFileName);
            if(!resource.exists()) {
                throw new Exception("Resource cannot be null: " + csvFileName);
            }
        } catch (Exception e) {
            throw new FileNotFoundException("File not found: " + csvFileName);
        }

        StockPriceResourceObject stockPriceResourceObject = new StockPriceResourceObject();

        double initialLowPrice = Double.MAX_VALUE;
        String initialBuyDate = "";

        String buyDate = "";
        String sellDate = "";
        double sellPrice = 0.0;
        double buyPrice = 0.0;
        double maxProfit = 0.0;

        String currentBuyDate;
        String currentSellDate;


        try (InputStream inputStream = resource.getInputStream();
             CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {

            String[] nextLine;

            if (reader.readNext() == null) {
                throw new IllegalArgumentException("CSV file is empty or missing header");
            }


            while ((nextLine = reader.readNext()) != null) {
                String dateStr = nextLine[0];

                int currentYear = Integer.parseInt(dateStr.split("-")[0]);

                if (currentYear == year) {
                    initialBuyDate = dateStr;
                    initialLowPrice = Double.parseDouble(nextLine[3]);
                    break;
                }
            }

            while ((nextLine = reader.readNext()) != null) {
                String dateStr = nextLine[0];
                int currentYear = Integer.parseInt(dateStr.split("-")[0]);

                if (currentYear == year && !dateStr.equals(initialBuyDate)) {
                    double currHighPrice = Double.parseDouble(nextLine[2]);
                    double currLowPrice = Double.parseDouble(nextLine[3]);
                    currentBuyDate = dateStr;
                    currentSellDate = dateStr;

                    double profit = currHighPrice - initialLowPrice;
                    if (profit > maxProfit) {
                        maxProfit = profit;
                        sellDate = currentSellDate;
                        buyDate = initialBuyDate;
                        sellPrice = currHighPrice;
                        buyPrice = initialLowPrice;
                    }

                    if (currLowPrice < initialLowPrice) {
                        initialLowPrice = currLowPrice;
                        initialBuyDate = currentBuyDate;
                    }
                }
            }

            if(maxProfit > 0.0) {
                stockPriceResourceObject.setBuyDate(LocalDate.parse(buyDate));
                stockPriceResourceObject.setBuyPrice(buyPrice);
                stockPriceResourceObject.setSellDate(LocalDate.parse(sellDate));
                stockPriceResourceObject.setSellPrice(sellPrice);
                stockPriceResourceObject.setProfit(maxProfit);
            }
            else {
                logger.info("No profit for {} in {}", stockName, year);
                stockPriceResourceObject.setBuyDate(LocalDate.parse(initialBuyDate));
                stockPriceResourceObject.setBuyPrice(initialLowPrice);
                stockPriceResourceObject.setSellDate(null);
                stockPriceResourceObject.setSellPrice(null);
                stockPriceResourceObject.setProfit(0.0);
            }
        }
        catch (IOException | CsvValidationException e) {
            logger.error("Error processing CSV file", e);
        }
    return stockPriceResourceObject;
    }
}