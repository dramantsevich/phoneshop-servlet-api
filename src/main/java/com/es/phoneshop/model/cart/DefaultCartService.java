package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";
    private ProductDao productDao;

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
    public synchronized void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        List<CartItem> cartList = cart.getItems();
        Product product = productDao.findProductById(productId);
        CartItem cartItem = new CartItem(product, quantity);

        if(cartList.isEmpty()) {
            if(product.getStock() > quantity) {
                cartList.add(cartItem);
            }
        } else {
            addingQuantityToExistingProducts(cartList, product, cartItem, quantity);
        }
    }

    private void addingQuantityToExistingProducts(List<CartItem> cartList, Product product, CartItem cartItem, int quantity) throws OutOfStockException {
        CartItem item = getCartItemByProductCode(cartList, cartItem);
        Optional<CartItem> result = cartList.stream()
                .filter(cl -> cl.getProduct().getCode().equals(cartItem.getProduct().getCode()))
                .findFirst();

        if(result.isPresent()) {
            if(product.getStock() < quantity || product.getStock() < (item.getQuantity() + quantity)) {
                throw new OutOfStockException(product, quantity, product.getStock());
            }

            if(product.getStock() >= (item.getQuantity() + quantity)) {
                item.setQuantity(item.getQuantity() + quantity);
            }
        } else {
            if(product.getStock() >= quantity) {
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
