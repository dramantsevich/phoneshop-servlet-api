package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class CartPageServlet extends HttpServlet {
    private CartDao cartDao;

    @Override
    public void init(ServletConfig config) {
        cartDao = ArrayListCartDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("cart", cartDao.getCart(request));

        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");

        Map<Long, String> errors = new HashMap<>();

        for (int i = 0; i < productIds.length; i++) {
            Long productId = Long.valueOf(productIds[i]);

            int quantity;
            Cart cart = cartDao.getCart(request);
            try {
                NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());
                quantity = numberFormat.parse(quantities[i]).intValue();

                cartDao.update(cart, productId, quantity);
            } catch (ParseException | OutOfStockException e) {
                HandleError.handleProductsError(errors, productId, e);
            }
        }

        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?message=Cart updated successfully");
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }
}
