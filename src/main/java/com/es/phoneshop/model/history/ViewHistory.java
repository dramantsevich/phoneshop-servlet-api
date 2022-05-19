package com.es.phoneshop.model.history;

import com.es.phoneshop.model.Entity;
import com.es.phoneshop.model.product.Product;

import java.util.ArrayList;
import java.util.List;

public class ViewHistory extends Entity {
    private final List<Product> items;

    public ViewHistory() {
        this.items = new ArrayList<>();
    }

    public List<Product> getItems() {
        return items;
    }
}
