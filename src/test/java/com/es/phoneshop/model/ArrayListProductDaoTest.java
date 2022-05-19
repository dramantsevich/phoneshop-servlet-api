package com.es.phoneshop.model;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

public class ArrayListProductDaoTest
{
    private ArrayListProductDao productDao;

    @Before
    public void setup() throws ProductNotFoundException {
        productDao = ArrayListProductDao.getInstance();
        saveSampleProducts();
    }

    @Test
    public void testGetItem() throws ProductNotFoundException {
        Product product = createProduct(null);
        Product result = productDao.getItem(product.getId());

        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo("test-product");
    }

    @Test
    public void testSaveWithNotNullId() throws ProductNotFoundException {
        Product product = createProduct(3L);
        Product result = productDao.getItem(product.getId());

        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo("test-product");
    }

    @Test
    public void testFindAscDescriptionSortedProductsWithoutQuery() {
        SortField sortField = SortField.DESCRIPTION;
        SortOrder sortOrder = SortOrder.ASC;
        List<Product> productSortedList = productDao.getAllProducts();
        Comparator<Product> comparator = sortComparator(sortField);

        productSortedList = productSortedList.stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        assertThat(productDao.getSortedProducts("",sortField, sortOrder)).isEqualTo(productSortedList);
    }

    @Test
    public void testFindDescDescriptionSortedProductsWithoutQuery() {
        SortField sortField = SortField.DESCRIPTION;
        SortOrder sortOrder = SortOrder.DESC;
        List<Product> productSortedList = productDao.getAllProducts();
        Comparator<Product> comparator = sortComparator(sortField);

        productSortedList = productSortedList.stream()
                .sorted(comparator.reversed())
                .collect(Collectors.toList());

        assertThat(productDao.getSortedProducts("",sortField, sortOrder)).isEqualTo(productSortedList);
    }

    @Test
    public void testFindAscPriceSortedProductsWithoutQuery() {
        SortField sortField = SortField.PRICE;
        SortOrder sortOrder = SortOrder.ASC;
        List<Product> productSortedList = productDao.getAllProducts();
        Comparator<Product> comparator = sortComparator(sortField);

        productSortedList = productSortedList.stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        assertThat(productDao.getSortedProducts("",sortField, sortOrder)).isEqualTo(productSortedList);
    }

    @Test
    public void testFindDescPriceSortedProductsWithoutQuery() {
        SortField sortField = SortField.PRICE;
        SortOrder sortOrder = SortOrder.DESC;
        List<Product> productSortedList = productDao.getAllProducts();
        Comparator<Product> comparator = sortComparator(sortField);

        productSortedList = productSortedList.stream()
                .sorted(comparator.reversed())
                .collect(Collectors.toList());

        assertThat(productDao.getSortedProducts("",sortField, sortOrder)).isEqualTo(productSortedList);
    }

    @Test
    public void testGetSortedProductsWithQuery() {
        String query = "Samsung Galaxy S";
        String sortField = "PRICE";
        String sortOrder = "ASC";

        assertThat(productDao.getSortedProducts(query,
                Optional.ofNullable(sortField).map(SortField::valueOf).orElse(null),
                Optional.ofNullable(sortOrder).map(SortOrder::valueOf).orElse(null))).isNotEmpty();
    }

    private static Comparator<Product> sortComparator(SortField sortField) {
        return Comparator.comparing(product -> {
            if(SortField.DESCRIPTION == sortField){
                return (Comparable) product.getDescription();
            } else {
                return (Comparable) product.getPrice();
            }
        });
    }

    private Product createProduct(Long id) {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product(id, "test-product", "Samsung Galaxy S", new BigDecimal(100), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        assertThat(product.getId()).isNotNull();

        return product;
    }

    private void saveSampleProducts() {
        Currency usd = Currency.getInstance("USD");
        productDao.save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd,
                5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        productDao.save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd,
                100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd,
                0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        productDao.save(new Product("iphone", "Apple iPhone", new BigDecimal(200), usd,
                10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        productDao.save(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd,
                30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        productDao.save(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd,
                3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        productDao.save(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), usd,
                30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        productDao.save(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd,
                100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        productDao.save(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), usd,
                100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        productDao.save(new Product("palmp", "Palm Pixi", new BigDecimal(170), usd,
                30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        productDao.save(new Product("simc56", "Siemens C56", new BigDecimal(70), usd,
                20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        productDao.save(new Product("simc61", "Siemens C61", new BigDecimal(80), usd,
                30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        productDao.save(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd,
                40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));
    }
}
