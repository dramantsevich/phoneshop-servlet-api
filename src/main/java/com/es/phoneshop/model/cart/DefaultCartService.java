package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";
    private ArrayListProductDao productDao;

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
    public synchronized Cart getCart(HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);

        if(cart == null){
            request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
        }

        return cart;
    }

    @Override
    public synchronized void clearCart(Cart cart) {
        List<CartItem> cartList = cart.getItems();

        cartList.removeIf(cartItem -> cartItem.getQuantity() > 0);
        cart.setTotalCost(new BigDecimal(0));
        cart.setTotalQuantity(0);
    }

    @Override
    public synchronized void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        List<CartItem> cartList = cart.getItems();
        Product product = productDao.getItemById(productId);
        CartItem cartItem = new CartItem(product, quantity);

        Optional<CartItem> optionalCartItem = findCartItemForUpdate(cart, productId, quantity);
        int productsAmount = optionalCartItem.map(CartItem::getQuantity).orElse(0);

        if(product.getStock() < productsAmount + quantity) {
            throw new OutOfStockException(product, productsAmount + quantity, product.getStock());
        }

        if(optionalCartItem.isPresent()) {
            optionalCartItem.get().setQuantity(productsAmount + quantity);
        } else {
            cartList.add(cartItem);
        }

        recalculateCartQuantity(cart);
        recalculateCartTotalCost(cart);
    }

    @Override
    public synchronized void update(Cart cart, Long productId, int quantity) throws OutOfStockException {
        List<CartItem> cartList = cart.getItems();
        Product product = productDao.getItemById(productId);
        CartItem cartItem = new CartItem(product, quantity);

        Optional<CartItem> cartItemOptional = findCartItemForUpdate(cart, productId, quantity);

        if(product.getStock() < quantity) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }

        if(cartItemOptional.isPresent()) {
            cartItemOptional.get().setQuantity(quantity);
        } else {
            cartList.add(cartItem);
        }

        recalculateCartQuantity(cart);
        recalculateCartTotalCost(cart);
    }

    @Override
    public synchronized void delete(Cart cart, Long productId) {
        cart.getItems().removeIf(item ->
                productId.equals(item.getProduct().getId())
        );

        recalculateCartQuantity(cart);
        recalculateCartTotalCost(cart);
    }

    private void recalculateCartQuantity(Cart cart) {
        cart.setTotalQuantity(cart.getItems().stream()
                .map(CartItem::getQuantity)
                .collect(Collectors.summingInt(q -> q.intValue()))
        );
    }

    private void recalculateCartTotalCost(Cart cart) {
        BigDecimal totalCost = BigDecimal.ZERO;

        for(CartItem item : cart.getItems()) {
            totalCost = totalCost.add(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        cart.setTotalCost(totalCost);
    }

    private Optional<CartItem> findCartItemForUpdate(Cart cart, Long productId, int quantity) throws OutOfStockException {
        if(quantity <= 0) {
            throw new OutOfStockException(null, quantity, 0);
        }

        List<CartItem> cartList = cart.getItems();
        Product product = productDao.getItemById(productId);
        CartItem cartItem = new CartItem(product, quantity);

        return cartList.stream()
                .filter(c -> c.getProduct().getId().equals(cartItem.getProduct().getId()))
                .findAny();
    }
}
