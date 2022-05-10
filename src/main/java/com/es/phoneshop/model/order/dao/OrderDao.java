package com.es.phoneshop.model.order.dao;

import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderNotFoundException;

public interface OrderDao {
    Order getOrder(Long id) throws OrderNotFoundException;
    void save(Order order) throws OrderNotFoundException;
}
