package com.stockprice.models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StockPriceResourceObject {

    private LocalDate buyDate;
    private Double buyPrice;
    private LocalDate sellDate;
    private Double sellPrice;
    private Double profit;
}
