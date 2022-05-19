package com.es.phoneshop.model.product;

import com.es.phoneshop.model.Entity;

import java.math.BigDecimal;

public class PriceHistory extends Entity {
    private String startDate;
    private BigDecimal price;

    public PriceHistory() {
    }

    public PriceHistory(String startDate, BigDecimal price) {
        this.startDate = startDate;
        this.price = price;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
