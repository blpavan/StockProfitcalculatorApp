package com.stockprice.service;

import com.stockprice.models.StockPriceResourceObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
    private ResourceLoader resourceLoader;
    @Mock
    private Resource resource;

    @InjectMocks
    private StockProfitCalculatorService stockProfitCalculatorService;



    @Test
    void testMaxProfitWhenNoFileFound(){
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

        FileNotFoundException thrown = assertThrows(FileNotFoundException.class, () -> stockProfitCalculatorService.maxProfit(stockName, year));

        assertEquals("File not found: " + csvFileName, thrown.getMessage());
    }

    @Test
    void testMaxProfitWithEmptyCSV() throws Exception {
        String emptyCSVContent = "";
        InputStream inputStream = new ByteArrayInputStream(emptyCSVContent.getBytes());

        Resource mockResource = mock(Resource.class);
        when(mockResource.exists()).thenReturn(true);
        when(mockResource.getInputStream()).thenReturn(inputStream);
        when(resourceLoader.getResource(anyString())).thenReturn(mockResource);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> stockProfitCalculatorService.maxProfit("AAPL", 2023));

        assertTrue(exception.getMessage().contains("CSV file is empty"));
    }

    @Test
    void testMaxProfitValidScenarioWithProfit() throws Exception {
        String csvContent = """
                Date,Open,High,Low,Close
                2023-01-01,100.0,105.0,99.0,102.0
                2023-01-02,102.0,110.0,101.0,108.0
                2023-01-03,108.0,115.0,107.0,111.0
                """;

        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        Resource mockResource = mock(Resource.class);
        when(mockResource.exists()).thenReturn(true);
        when(mockResource.getInputStream()).thenReturn(inputStream);
        when(resourceLoader.getResource(anyString())).thenReturn(mockResource);

        StockPriceResourceObject result = stockProfitCalculatorService.maxProfit("csvContent", 2023);

        assertNotNull(result);
        assertEquals(99.0, result.getBuyPrice(), 0.0001);
        assertEquals(115.0, result.getSellPrice(), 0.0001);
        assertEquals(16.0, result.getProfit(), 0.0001); // Max profit = 115 - 99 = 16
        assertEquals("2023-01-03", result.getSellDate().toString());
    }

    @Test
    void testMaxProfitValidScenarioWithNoProfit() throws Exception {
        String csvContent = """
                Date,Open,High,Low,Close
                2023-01-01,100.0,105.0,99.0,102.0
                2023-01-02,99.0,99.0,99.0,99.0
                2023-01-03,98.0,97.0,96.0,96.0
                """;

        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        Resource mockResource = mock(Resource.class);
        when(mockResource.exists()).thenReturn(true);
        when(mockResource.getInputStream()).thenReturn(inputStream);
        when(resourceLoader.getResource(anyString())).thenReturn(mockResource);

        StockPriceResourceObject result = stockProfitCalculatorService.maxProfit("AAPL", 2023);

        assertNotNull(result);
        assertEquals(96.0, result.getBuyPrice(), 0.0001);
        assertNull(result.getSellDate());
        assertNull(result.getSellPrice());
        assertEquals(0.0, result.getProfit(), 0.0001);
    }

    @Test
    void testMaxProfitScenarioWithUpdatedBuyAfterFindingLowerPrice() throws Exception {
        String csvContent = """
                Date,Open,High,Low,Close
                2023-01-01,100.0,105.0,99.0,102.0
                2023-01-02,102.0,110.0,101.0,108.0
                2023-01-03,103.0,115.0,90.0,103.0
                2023-01-04,103.0,120.0,100.0,110.0
                """;

        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        Resource mockResource = mock(Resource.class);
        when(mockResource.exists()).thenReturn(true);
        when(mockResource.getInputStream()).thenReturn(inputStream);
        when(resourceLoader.getResource(anyString())).thenReturn(mockResource);

        StockPriceResourceObject result = stockProfitCalculatorService.maxProfit("AAPL", 2023);

        assertNotNull(result);
        assertEquals(90.0, result.getBuyPrice(), 0.0001);
        assertEquals(120.0, result.getSellPrice(), 0.0001);
        assertEquals(30.0, result.getProfit(), 0.0001);
        assertEquals("2023-01-04", result.getSellDate().toString());
    }

    @Test
    void testOnlyOneDayOfData() throws Exception {
        // Test with only one day's worth of data
        String csvContent = """
                Date,Open,High,Low,Close
                2023-01-01,100.0,105.0,99.0,102.0
                """;

        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        Resource mockResource = mock(Resource.class);
        when(mockResource.exists()).thenReturn(true);
        when(mockResource.getInputStream()).thenReturn(inputStream);
        when(resourceLoader.getResource(anyString())).thenReturn(mockResource);

        StockPriceResourceObject result = stockProfitCalculatorService.maxProfit("AAPL", 2023);

        assertNotNull(result);
        assertEquals(99.0, result.getBuyPrice(), 0.0001);
        assertNull(result.getSellDate());
        assertNull(result.getSellPrice());
        assertEquals(0.0, result.getProfit(), 0.0001);
    }

    @Test
    void testBuyAndSellOnConsecutiveDays() throws Exception {
        String csvContent = """
                Date,Open,High,Low,Close
                2023-01-01,100.0,105.0,99.0,102.0
                2023-01-02,102.0,110.0,101.0,108.0
                """;

        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        Resource mockResource = mock(Resource.class);
        when(mockResource.exists()).thenReturn(true);
        when(mockResource.getInputStream()).thenReturn(inputStream);
        when(resourceLoader.getResource(anyString())).thenReturn(mockResource);

        StockPriceResourceObject result = stockProfitCalculatorService.maxProfit("AAPL", 2023);

        assertNotNull(result);
        assertEquals(99.0, result.getBuyPrice(), 0.0001);
        assertEquals(110.0, result.getSellPrice(), 0.0001);
        assertEquals(11.0, result.getProfit(), 0.0001);
        assertEquals("2023-01-02", result.getSellDate().toString());
    }


}
