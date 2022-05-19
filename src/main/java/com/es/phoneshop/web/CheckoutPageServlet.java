package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.*;
import com.es.phoneshop.model.order.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CheckoutPageServlet extends HttpServlet {
    private CartDao cartDao;
    private OrderDao orderDao;
    private DefaultOrderService orderService;

    @Override
    public void init(ServletConfig config) {
        cartDao = ArrayListCartDao.getInstance();
        orderDao = ArrayListOrderDao.getInstance();
        orderService = DefaultOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartDao.getCart(request);
        request.setAttribute("order", orderDao.getOrder(cart));
        request.setAttribute("paymentMethods", orderService.getPaymentMethod());

        request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartDao.getCart(request);
        Order order = orderDao.getOrder(cart);
        Map<String, String> errors = new HashMap<>();

        orderService.setRequiredParameter(request, "firstName", errors, order::setFirstName);
        orderService.setRequiredParameter(request, "lastName", errors, order::setLastName);
        orderService.setRequiredParameter(request, "phone", errors, order::setPhone);
        orderService.setDeliveryDate(request, errors, order);
        orderService.setRequiredParameter(request, "deliveryAddress", errors, order::setDeliveryAddress);
        orderService.setPaymentMethod(request, errors, order);

        HandleError.handleCheckoutError(request, response, errors, order);
    }

}
