package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ArrayListCartDao implements CartDao {
    private static final String CART_SESSION_ATTRIBUTE = ArrayListCartDao.class.getName() + ".cart";
    private static final ArrayListCartDao INSTANCE = new ArrayListCartDao();
    private final ArrayListProductDao productDao;
    private final DefaultCartService cartService;

    private ArrayListCartDao() {
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    public static ArrayListCartDao getInstance() {
        return INSTANCE;
    }


    @Override
    public Cart getCart(HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);

        if (cart == null) {
            request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
        }

        return cart;
    }

    @Override
    public synchronized void clearCart(Cart cart) {
        List<CartItem> cartList = cart.getItems();

        cartList.clear();
        cart.setTotalCost(new BigDecimal(0));
        cart.setTotalQuantity(0);
    }

    @Override
    public synchronized void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Optional<CartItem> optionalCartItem = cartService.findCartItemForUpdate(cart, productId, quantity);
        int productsAmount = optionalCartItem.map(CartItem::getQuantity).orElse(0);

        Product product = productDao.getItem(productId);
        if (product.getStock() < productsAmount + quantity) {
            throw new OutOfStockException(product, productsAmount + quantity, product.getStock());
        }

        List<CartItem> cartList = cart.getItems();
        CartItem cartItem = new CartItem(product, quantity);

        if (optionalCartItem.isPresent()) {
            optionalCartItem.get().setQuantity(productsAmount + quantity);
        } else {
            cartList.add(cartItem);
        }

        cartService.recalculateCartQuantity(cart);
        cartService.recalculateCartTotalCost(cart);
    }

    @Override
    public synchronized void update(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getItem(productId);

        if (product.getStock() < quantity) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }

        Optional<CartItem> cartItemOptional = cartService.findCartItemForUpdate(cart, productId, quantity);

        List<CartItem> cartList = cart.getItems();
        CartItem cartItem = new CartItem(product, quantity);
        if (cartItemOptional.isPresent()) {
            cartItemOptional.get().setQuantity(quantity);
        } else {
            cartList.add(cartItem);
        }

        cartService.recalculateCartQuantity(cart);
        cartService.recalculateCartTotalCost(cart);
    }

    @Override
    public synchronized void delete(Cart cart, Long productId) {
        cart.getItems().removeIf(item ->
                productId.equals(item.getProduct().getId())
        );

        cartService.recalculateCartQuantity(cart);
        cartService.recalculateCartTotalCost(cart);
    }

}
