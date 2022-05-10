package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.product.ProductNotFoundException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddProductToCartServlet extends HttpServlet {
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws  ServletException{
        super.init(config);
        try {
            cartService = DefaultCartService.getInstance();
        } catch (ProductNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");

        Map<Long, String> errors = new HashMap<>();

        for(int i = 0; i < productIds.length; i++) {
            if(!quantities[i].isEmpty() || quantities[i] != null || !quantities[i].equals("")){
                Long productId = Long.valueOf(productIds[i]);
                try {
                    int quantity = Integer.parseInt(quantities[i]);
                    Cart cart = cartService.getCart(request);
                    cartService.add(cart, productId, quantity);
                } catch (NumberFormatException | OutOfStockException e) {
                    handleError(errors, productId, e);
                }
            }
        }

        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products?message=Product added to cart successfully");
        } else {
            request.setAttribute("errors", errors);
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }

    private void handleError(Map<Long, String> errors, Long productId, Exception e) {
        if(e.getClass().equals(NumberFormatException.class)) {
            errors.put(productId, "Not a number");
        } else {
            if(((OutOfStockException) e).getStockRequested() <= 0) {
                errors.put(productId, "Can't be negative or zero");
            } else {
                errors.put(productId, "Out of stock, max available " + ((OutOfStockException) e).getStockAvailable());
            }
        }
    }
}