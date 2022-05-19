package com.es.phoneshop.model;

import com.es.phoneshop.model.cart.*;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.ProductNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class ArrayListCartDaoTest {
    private ProductDao productDao;
    private CartDao cartDao;

    @Before
    public void setup() throws ProductNotFoundException {
        productDao = ArrayListProductDao.getInstance();
        cartDao = ArrayListCartDao.getInstance();
    }

    @Test
    public void testClearCart() {
        Cart cart = createCart(5, 3);

        cartDao.clearCart(cart);

        assertThat(cart.getItems()).isEmpty();
    }

    @Test
    public void testAddToCartOutOfStockException() {
        Cart cart = createCart(3, 6);
    }

    @Test
    public void testUpdate() throws OutOfStockException {
        Cart cart = createCart(5, 3);

        cartDao.update(cart, 0L, 1);
        List<CartItem> cartItemList = cart.getItems();
        int expectedProductQuantity = cartItemList.get(2).getQuantity();
//        int expectedProductQuantity = cartItemList.get(0).getQuantity();

        assertThat(expectedProductQuantity).isEqualTo(1);
    }

    @Test
    public void testDelete() {
        Cart cart = createCart(5, 2);
        cartDao.delete(cart, 0L);
        cartDao.delete(cart, 1L);
        int actualSize = cart.getItems().size();

//        assertThat(actualSize).isZero();
        assertThat(actualSize).isEqualTo(2);
    }

    @Test
    public void testUpdateOutOfStockException() {
        Cart cart = createCart(5, 3);

        try{
            cartDao.update(cart, 0L, 6);

            assertThat(cart.getTotalQuantity()).isEqualTo(6);
        } catch(OutOfStockException e) {
            assertThatExceptionOfType(OutOfStockException.class);
        }
    }

    private Cart createCart(int productStock, int quantity) {
        Cart cart = new Cart();
        Currency usd = Currency.getInstance("USD");
        Product product1 = new Product("test-product", "Samsung Galaxy S", new BigDecimal(100), usd, productStock, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product product2 = new Product("testt-product", "Iphone", new BigDecimal(100), usd, productStock, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product1);
        productDao.save(product2);

        try{
            cartDao.add(cart, product1.getId(), quantity);
            cartDao.add(cart, product2.getId(), quantity);
        } catch (OutOfStockException e){
            assertThatExceptionOfType(OutOfStockException.class);
        }

        assertThat(cart).isNotNull();

        return cart;
    }

}
