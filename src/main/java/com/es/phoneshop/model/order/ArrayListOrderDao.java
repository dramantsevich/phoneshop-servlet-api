package com.es.phoneshop.model.order;

import com.es.phoneshop.model.GenericDao;

import java.util.*;

public class ArrayListOrderDao extends GenericDao<Order, String> {
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
    public synchronized Order getItem(String id) {
        Long longId = Long.valueOf(id);

        return orderList.stream()
                .filter(o -> longId.equals(o.getId()))
                .findAny()
                .orElseThrow(OrderNotFoundException::new);
    }

    public synchronized Order getItemBySecureId(String id) {
        return orderList.stream()
                .filter(o -> id.equals(o.getSecureId()))
                .findAny()
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public synchronized void save(Order item) {
        Long longId = item.getId();

        if(longId != null) {
            String id = item.getId().toString();
            orderList.remove(getItem(id));
            orderList.add(item);
        } else {
            item.setId(++orderId);
            orderList.add(item);
        }
    }
}
