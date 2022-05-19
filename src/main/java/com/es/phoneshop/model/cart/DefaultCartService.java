package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ArrayListProductDao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class DefaultCartService {
    private static final DefaultCartService INSTANCE = new DefaultCartService();
    private final ArrayListProductDao productDao;

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static DefaultCartService getInstance() {
        return INSTANCE;
    }

    protected void recalculateCartQuantity(Cart cart) {
        int totalQuantity = cart.getItems().stream()
                .map(CartItem::getQuantity).mapToInt(Integer::intValue).sum();

        cart.setTotalQuantity(totalQuantity);
    }

    protected void recalculateCartTotalCost(Cart cart) {
        BigDecimal totalCost = cart.getItems().stream()
                .map(item -> BigDecimal.valueOf(item.getQuantity()).multiply(item.getProduct().getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalCost(totalCost);
    }

    protected Optional<CartItem> findCartItemForUpdate(Cart cart, Long productId, int quantity) throws OutOfStockException {
        if (quantity <= 0) {
            throw new OutOfStockException(null, quantity, 0);
        }

        List<CartItem> cartList = cart.getItems();
        Product product = productDao.getItem(productId);
        CartItem cartItem = new CartItem(product, quantity);

        return cartList.stream()
                .filter(c -> c.getProduct().getId().equals(cartItem.getProduct().getId()))
                .findAny();
    }
}
