package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;

import java.util.List;
import java.util.Optional;

public class DefaultCartService implements CartService {
    private ProductDao productDao;
    private Cart cart = new Cart();

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    private static class SingletonHelper {
        private static final DefaultCartService INSTANCE = new DefaultCartService();
    }

    public static DefaultCartService getInstance() {
        return DefaultCartService.SingletonHelper.INSTANCE;
    }


    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public void add(Long productId, int quantity) {
        List<CartItem> cartList = cart.getItems();

        Product product = productDao.findProductById(productId);

        CartItem cartItem = new CartItem(product, quantity);

        if(cartList.isEmpty()) {
            cartList.add(cartItem);
        } else {
            Optional<CartItem> result = cartList.stream()
                    .filter(cl -> cl.getProduct().getCode().equals(cartItem.getProduct().getCode()))
                    .findFirst();

            if(result.isPresent()) {
                CartItem item = getCartItemByProductCode(cartList, cartItem);
                
                item.setQuantity(item.getQuantity() + quantity);
            } else {
                cartList.add(cartItem);
            }
        }
    }

    private CartItem getCartItemByProductCode(List<CartItem> cartItemList, CartItem cartItem) {
        CartItem result = null;

        for(CartItem item : cartItemList) {
            if(item.getProduct().getCode() == cartItem.getProduct().getCode()) {
                result = item;
            }
        }

        return result;
    }
}
