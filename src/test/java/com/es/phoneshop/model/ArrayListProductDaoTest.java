package com.es.phoneshop.model;

import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.ProductNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;

    @Before
    public void setup() throws ProductNotFoundException {
        productDao = ArrayListProductDao.getInstance();
    }

    @Test
    public void testFindProductsNoResults() {
        assertTrue(productDao.findAllProducts().isEmpty());
    }

    @Test
    public void testSaveNewProduct() throws ProductNotFoundException {
        Product product = createProduct();
        Product result = productDao.findProductById(product.getId());

        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo("test-product");
    }

    @Test
    public void testFindProductWithZeroStock() throws ProductNotFoundException {
        Product product = createProduct();
        Product result = productDao.findProductById(product.getId());

        assertThat(result).isNotNull();
        assertThat(product.getStock()).isZero();
    }

    @Test
    public void testDeleteProduct() {
        Product product = createProduct();

        productDao.delete(product);
        List<Product> productList = productDao.findAllProducts();

        assertThat(productList).isNotNull()
                .doesNotContain(product);
    }

    @Test
    public void testDeleteProductById() throws ProductNotFoundException {
        Product product = createProduct();

        productDao.deleteById(product.getId());
        List<Product> productList = productDao.findAllProducts();

        assertThat(productList).isNotNull()
                .doesNotContain(product);
    }

    @Test
    public void testFindProducts() {
        Product product = createProduct();
        List<Product> result = productDao.findProducts(product.getDescription());

        assertThat(result).isNotEmpty();
    }

    @Test
    public void testFindAscDescriptionSortedProducts() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        SortField sortField = SortField.description;
        SortOrder sortOrder = SortOrder.asc;
        List<Product> productSortedList = productDao.findAllProducts();
        Method sortComparator = ArrayListProductDao.class.getDeclaredMethod("sortComparator", SortField.class);

        sortComparator.setAccessible(true);

        Comparator<Product> comparator = (Comparator<Product>) sortComparator.invoke(ArrayListProductDao.class, sortField);
        productSortedList = productSortedList.stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        assertThat(productDao.findSortedProducts(sortField, sortOrder)).isEqualTo(productSortedList);
    }

    @Test
    public void testFindDescPriceSortedProducts() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        SortField sortField = SortField.price;
        SortOrder sortOrder = SortOrder.desc;
        List<Product> productSortedList = productDao.findAllProducts();
        Method sortComparator = ArrayListProductDao.class.getDeclaredMethod("sortComparator", SortField.class);

        sortComparator.setAccessible(true);

        Comparator<Product> comparator = (Comparator<Product>) sortComparator.invoke(ArrayListProductDao.class, sortField);
        productSortedList = productSortedList.stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        assertThat(productDao.findSortedProducts(sortField, sortOrder)).isEqualTo(productSortedList);
    }

    @Test
    public void testFindRecentlyViewedProducts() {
        Deque<Long> recentlyViewedProductsId = new ArrayDeque<>();
        int[] productsId = {0, 4, 6, 8, 9};

        for(int i : productsId) {
            recentlyViewedProductsId = addRecentlyViewedProducts((long) i);
        }

        List<Product> productsList = productDao.findRecentlyViewedProducts(recentlyViewedProductsId);

        for(Product product : productsList) {
            for(Long id : recentlyViewedProductsId) {
                assertThat(product.getId()).isEqualTo(id);
            }
        }
    }

    private Deque<Long> addRecentlyViewedProducts(Long productId) {
        Deque<Long> recentlyViewedProductsId = new ArrayDeque<>();

        if(recentlyViewedProductsId.size() < 3) {
            recentlyViewedProductsId.add(productId);
        } else {
            recentlyViewedProductsId.removeFirst();
            recentlyViewedProductsId.addLast(productId);
        }

        return recentlyViewedProductsId;
    }

    private Product createProduct() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("test-product", "Samsung Galaxy S", new BigDecimal(100), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.create(product);

        assertThat(product.getId()).isNotNull();

        return product;
    }
}
