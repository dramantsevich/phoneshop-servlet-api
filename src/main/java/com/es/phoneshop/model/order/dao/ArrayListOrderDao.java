package com.es.phoneshop.model.order.dao;

import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderNotFoundException;

import java.util.*;

public class ArrayListOrderDao implements OrderDao {
    private long orderId;
    private List<Order> orderList;

    private static ArrayListOrderDao instance;

    public static ArrayListOrderDao getInstance() {
        if(instance == null){
            instance = new ArrayListOrderDao();
        }
        return instance;
    }

    private ArrayListOrderDao() {
        this.orderList = new ArrayList<>();
    }

    @Override
    public synchronized Order getOrder(Long id) throws OrderNotFoundException {
        return orderList.stream()
                .filter(o -> id.equals(o.getId()))
                .findAny()
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public Order getOrderBySecureId(String secureId) throws OrderNotFoundException {
        return orderList.stream()
                .filter(o -> secureId.equals(o.getSecureId()))
                .findAny()
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public synchronized void save(Order order) throws OrderNotFoundException {
        Long id = order.getId();

        if(id != null) {
            orderList.remove(getOrder(id));
            orderList.add(order);
        } else {
            order.setId(++orderId);
            orderList.add(order);
        }
    }
}
