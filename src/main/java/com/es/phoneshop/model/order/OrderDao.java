package com.es.phoneshop.model.order;

import com.es.phoneshop.model.GenericDao;
import com.es.phoneshop.model.cart.Cart;


public interface OrderDao extends GenericDao<Order, Long> {
    Order getOrder(Cart cart);

    Order getItemBySecureId(String id);

    void placeOrder(Order order);
}
