package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.Entity;

import java.util.ArrayList;
import java.util.List;

public class Cart extends Entity {
    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public List<CartItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Cart" + items;
    }
}
