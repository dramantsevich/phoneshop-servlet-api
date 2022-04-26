package com.es.phoneshop.model;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.ProductNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;

    @Before
    public void setup() throws ProductNotFoundException {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findAll().isEmpty());
    }

    @Test
    public void testSaveNewProduct() throws ProductNotFoundException {
        Product product = createProduct();

        assertTrue(product.getId() > 0);
        Product result = productDao.findEntityById(product.getId());
        assertNotNull(result);
        assertEquals("test-product", result.getCode());
    }

    @Test
    public void testFindProductWithZeroStock() throws ProductNotFoundException {
        Product product = createProduct();

        assertNotNull(product.getId());
        Product result = productDao.findEntityById(product.getId());
        assertNotNull(result);
        assertEquals(0, product.getStock());
    }

    @Test
    public void testDeleteProduct() {
        Product product = createProduct();

        assertNotNull(product.getId());
        productDao.delete(product);
        List<Product> productList = productDao.findAll();
        assertFalse(productList.contains(product));
    }

    @Test
    public void testDeleteProductById() throws ProductNotFoundException {
        Product product = createProduct();

        assertNotNull(product.getId());
        productDao.deleteById(product.getId());
        List<Product> productList = productDao.findAll();
        assertFalse(productList.contains(product));
    }

    private Product createProduct() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("test-product", "Samsung Galaxy S", new BigDecimal(100), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.create(product);

        return product;
    }
}
