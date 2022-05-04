package com.es.phoneshop.model.product;

import java.math.BigDecimal;

public class PriceHistory {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PriceHistory that = (PriceHistory) o;

        if (!startDate.equals(that.startDate)) return false;
        return price.equals(that.price);
    }

    @Override
    public int hashCode() {
        int result = startDate.hashCode();
        result = 31 * result + price.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PriceHistory{" +
                "startDate='" + startDate + '\'' +
                ", price=" + price +
                '}';
    }
}
