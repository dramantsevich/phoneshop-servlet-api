package com.es.phoneshop.model.cart;

import javax.servlet.http.HttpServletRequest;

public interface CartDao {
    Cart getCart(HttpServletRequest request);

    void add(Cart cart, Long productId, int quantity) throws OutOfStockException;

    void update(Cart cart, Long productId, int quantity) throws OutOfStockException;

    void delete(Cart cart, Long productId);

    void clearCart(Cart cart);
}
