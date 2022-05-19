package com.es.phoneshop.model;

import com.es.phoneshop.model.cart.*;
import com.es.phoneshop.model.order.*;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.ProductNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;

import static org.assertj.core.api.Assertions.*;

public class ArrayListOrderDaoTest {
    private OrderDao orderDao;
    private CartDao cartDao;
    private ProductDao productDao;
    private DefaultOrderService orderService;

    @Before
    public void setup() throws ProductNotFoundException {
        orderDao = ArrayListOrderDao.getInstance();
        cartDao = ArrayListCartDao.getInstance();
        productDao = ArrayListProductDao.getInstance();
        orderService = DefaultOrderService.getInstance();
    }

    @Test
    public void testGetOrder() {
        Order order = createOrder(5, 2);

        assertThat(order).isNotNull();
    }

    @Test
    public void testGetItemBySecureId() {
        Order order = createOrder(5, 2);
        orderDao.placeOrder(order);
        order.setId(123L);
        order.setSecureId("123");

        assertThat(orderDao.getItemBySecureId("123")).isNotNull();
    }

    @Test
    public void testGetPaymenthMethod() {
        assertThat(orderService.getPaymentMethod()).isEqualTo(Arrays.asList(PaymentMethod.values()));
    }

    @Test
    public void testSave() {
        Order order = createOrder(5, 2);
        orderDao.placeOrder(order);
        order.setId(0L);

        assertThat(orderDao.getItem(0L)).isNotNull();
    }

    private Order createOrder(int productStock, int quantity) {
        Cart cart = new Cart();
        Currency usd = Currency.getInstance("USD");
        Product product1 = new Product("test-product", "Samsung Galaxy S", new BigDecimal(100), usd, productStock, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product1);

        try{
            cartDao.add(cart, product1.getId(), quantity);
        } catch (OutOfStockException e){
            assertThatExceptionOfType(OutOfStockException.class);
        }

        return orderDao.getOrder(cart);
    }
}
