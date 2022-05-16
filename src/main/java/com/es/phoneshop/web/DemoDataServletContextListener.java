package com.es.phoneshop.web;

import com.es.phoneshop.model.product.PriceHistory;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductNotFoundException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Random;

public class DemoDataServletContextListener implements ServletContextListener {
    private final ArrayListProductDao productDao;

    public DemoDataServletContextListener() throws ProductNotFoundException {
        this.productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        boolean insertDemoData = Boolean.parseBoolean(event.getServletContext().getInitParameter("insertDemoData"));

        if (insertDemoData) {
            saveSampleProducts();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private void saveSampleProducts() {
        Currency usd = Currency.getInstance("USD");
        productDao.save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd,
                5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg", setPriceHistory(new BigDecimal(250))));
        productDao.save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd,
                100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg", setPriceHistory(new BigDecimal(90))));
        productDao.save(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd,
                0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg", setPriceHistory(new BigDecimal(220))));
        productDao.save(new Product("iphone", "Apple iPhone", new BigDecimal(200), usd,
                10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg", setPriceHistory(new BigDecimal(260))));
        productDao.save(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd,
                30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg", setPriceHistory(new BigDecimal(900))));
        productDao.save(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd,
                3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg", setPriceHistory(new BigDecimal(360))));
        productDao.save(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), usd,
                30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg", setPriceHistory(new BigDecimal(400))));
        productDao.save(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd,
                100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg", setPriceHistory(new BigDecimal(140))));
        productDao.save(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), usd,
                100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg", setPriceHistory(new BigDecimal(150))));
        productDao.save(new Product("palmp", "Palm Pixi", new BigDecimal(170), usd,
                30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg", setPriceHistory(new BigDecimal(100))));
        productDao.save(new Product("simc56", "Siemens C56", new BigDecimal(70), usd,
                20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg", setPriceHistory(new BigDecimal(75))));
        productDao.save(new Product("simc61", "Siemens C61", new BigDecimal(80), usd,
                30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg", setPriceHistory(new BigDecimal(75))));
        productDao.save(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd,
                40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg", setPriceHistory(new BigDecimal(200))));
    }

    private List<PriceHistory> setPriceHistory(BigDecimal price) {
        List<PriceHistory> priceHistories = new ArrayList<>();

        priceHistories.add(new PriceHistory(setRandomDate(), price));

        return priceHistories;
    }

    private String setRandomDate() {
        Random random = new Random();
        int minDay = (int) LocalDate.of(2015, 1, 1).toEpochDay();
        int maxDay = (int) LocalDate.of(2021, 1, 1).toEpochDay();
        long randomDay = minDay + random.nextInt(maxDay - minDay);

        LocalDate myDateObj = LocalDate.ofEpochDay(randomDay);
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

        return myDateObj.format(myFormatObj);
    }
}
