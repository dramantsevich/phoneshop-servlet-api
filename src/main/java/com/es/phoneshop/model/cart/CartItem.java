package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.Entity;
import com.es.phoneshop.model.product.Product;

public class CartItem extends Entity {
    private final Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public CartItem(CartItem cartItem) {
        this.product = cartItem.product;
        this.quantity = cartItem.quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
