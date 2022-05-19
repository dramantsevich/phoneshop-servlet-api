package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.ArrayListCartDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartDao;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.order.ArrayListOrderDao;
import com.es.phoneshop.model.order.DefaultOrderService;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

public class HandleError {
    private static final HandleError INSTANCE = new HandleError();
    private static CartDao cartDao;
    private static OrderDao orderDao;
    private static DefaultOrderService orderService;

    private HandleError() {
        cartDao = ArrayListCartDao.getInstance();
        orderDao = ArrayListOrderDao.getInstance();
        orderService = DefaultOrderService.getInstance();
    }

    public static HandleError getInstance() {
        return INSTANCE;
    }

    public static void handleProductsError(Map<Long, String> errors, Long productId, Exception e) {
        if (e.getClass().equals(ParseException.class)) {
            errors.put(productId, "Not a number");
        } else {
            if (((OutOfStockException) e).getStockRequested() <= 0) {
                errors.put(productId, "Can't be negative or zero");
            } else {
                errors.put(productId, "Out of stock, max available " + ((OutOfStockException) e).getStockAvailable());
            }
        }
    }

    public static void handleProductDetailError(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException{
        String attribute = "error";

        if (e.getClass().equals(ParseException.class)) {
            request.setAttribute(attribute, "Not a number");
        } else {
            if (((OutOfStockException) e).getStockRequested() <= 0) {
                request.setAttribute(attribute, "Can't be negative or zero");
            } else if (e.getClass().equals(OutOfStockException.class)) {
                request.setAttribute(attribute, "Out of stock, max available " + ((OutOfStockException) e).getStockAvailable());
            } else {
                request.setAttribute("message", "Product not found");
                response.sendError(404);
            }
        }
    }

    public static void handleCheckoutError(HttpServletRequest request, HttpServletResponse response, Map<String, String> errors,
                                    Order order) throws IOException, ServletException {
        Cart cart = cartDao.getCart(request);

        if (errors.isEmpty()) {
            orderDao.placeOrder(order);
            cartDao.clearCart(cart);
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());
        } else {
            request.setAttribute("cart", cart);
            request.setAttribute("errors", errors);
            request.setAttribute("order", order);
            request.setAttribute("paymentMethods", orderService.getPaymentMethod());
            request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
        }
    }
}
