package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;

import java.util.*;
import java.util.stream.Collectors;

public class ArrayListOrderDao implements OrderDao {
    private static final ArrayListOrderDao INSTANCE = new ArrayListOrderDao();
    private final DefaultOrderService orderService = DefaultOrderService.getInstance();
    private final List<Order> orderList;
    private long orderId;

    private ArrayListOrderDao() {
        this.orderList = new ArrayList<>();
    }

    public static ArrayListOrderDao getInstance() {
        return INSTANCE;
    }

    @Override
    public Order getItem(Long id) {
        return orderList.stream()
                .filter(o -> id.equals(o.getId()))
                .findAny()
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public synchronized void save(Order item) {
        Long longId = item.getId();

        if (longId != null) {
            Long id = item.getId();

            orderList.remove(getItem(id));
        } else {
            item.setId(++orderId);
        }

        orderList.add(item);
    }

    @Override
    public Order getOrder(Cart cart) {
        Order order = new Order();
        order.setItems(cart.getItems().stream().map(CartItem::new).collect(Collectors.toList()));
        order.setSubTotal(cart.getTotalCost());
        order.setDeliveryCost(orderService.calculateDeliveryCost());
        order.setTotalCost(order.getSubTotal().add(order.getDeliveryCost()));

        return order;
    }

    @Override
    public Order getItemBySecureId(String id) {
        return orderList.stream()
                .filter(o -> id.equals(o.getSecureId()))
                .findAny()
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public void placeOrder(Order order) {
        order.setSecureId(UUID.randomUUID().toString());
        save(order);
    }
}
