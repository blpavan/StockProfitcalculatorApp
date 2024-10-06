package com.stockprice.service;

import com.stockprice.models.StockPriceResourceObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StockProfitCalculatorServiceTest {
    @Mock
    private Logger logger;
    @Mock
    private ResourceLoader resourceLoader;
    @Mock
    private Resource resource;

    @InjectMocks
    private StockProfitCalculatorService stockProfitCalculatorService;



    @Test
    void testMaxProfitWhenNoFileFound() throws Exception {
        assertThrows(FileNotFoundException.class,() ->  stockProfitCalculatorService.maxProfit("RANDOM", 2014));
    }

    @Test
    void testMaxProfitWhenNoFileSuccessfullyLoaded() throws Exception {
        when(resourceLoader.getResource(any())).thenReturn(new ClassPathResource("MSFT.csv"));
        stockProfitCalculatorService.maxProfit("MSFT", 2014);
    }

    @Test
    void testMaxProfitResourceCannotBeNull() {
        String stockName = "AAPL";
        int year = 2023;
        String csvFileName = stockName.toUpperCase() + ".csv";

        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        when(resource.exists()).thenReturn(false); // Simulating that the resource does not exist

        FileNotFoundException thrown = assertThrows(FileNotFoundException.class, () -> {
            stockProfitCalculatorService.maxProfit(stockName, year);
        });

        assertEquals("File not found: " + csvFileName, thrown.getMessage());
    }

    @Test
    void testMaxProfitWithEmptyCSV() throws Exception {
        String emptyCSVContent = ""; // Empty content
        InputStream inputStream = new ByteArrayInputStream(emptyCSVContent.getBytes());

        Resource mockResource = mock(Resource.class);
        when(mockResource.exists()).thenReturn(true);
        when(mockResource.getInputStream()).thenReturn(inputStream);
        when(resourceLoader.getResource(anyString())).thenReturn(mockResource);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            stockProfitCalculatorService.maxProfit("AAPL", 2023);
        });

        assertTrue(exception.getMessage().contains("CSV file is empty"));
    }

    @Test
    void testMaxProfitWithInvalidDataTypes() throws Exception {
        String csvContent = "Date,Open,High,Low,Close\n" +
                "2023-01-01,invalid,105.0,99.0,102.0\n" +
                "2023-01-02,102.0,110.0,101.0,108.0\n";

        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        Resource mockResource = mock(Resource.class);
        when(mockResource.exists()).thenReturn(true);
        when(mockResource.getInputStream()).thenReturn(inputStream);
        when(resourceLoader.getResource(anyString())).thenReturn(mockResource);

        assertThrows(NumberFormatException.class, () -> {
            stockProfitCalculatorService.maxProfit("AAPL", 2023);
        });
    }


    @Test
    void testMaxProfitValidScenario() throws Exception {
        String csvContent = "Date,Open,High,Low,Close\n" +
                "2023-01-01,100.0,105.0,99.0,102.0\n" +
                "2023-01-02,102.0,110.0,101.0,108.0\n" +
                "2023-01-03,108.0,115.0,107.0,111.0\n";

        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        Resource mockResource = mock(Resource.class);
        when(mockResource.exists()).thenReturn(true);
        when(mockResource.getInputStream()).thenReturn(inputStream);
        when(resourceLoader.getResource(anyString())).thenReturn(mockResource);

        StockPriceResourceObject result = stockProfitCalculatorService.maxProfit("AAPL", 2023);

        assertNotNull(result);
        assertEquals(100.0, result.getBuyPrice(), 0.0001);
        assertEquals(111.0, result.getSellPrice(), 0.0001);
        assertEquals(11.0, result.getProfit(), 0.0001);
        assertEquals("2023-01-03", result.getSellDate().toString());
    }
}
