package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.*;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteCartItemServlet extends HttpServlet {
    private CartDao cartDao;

    @Override
    public void init(ServletConfig config) {
        cartDao = ArrayListCartDao.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String productId = request.getPathInfo().substring(1);

        Cart cart = cartDao.getCart(request);
        cartDao.delete(cart, Long.valueOf(productId));

        response.sendRedirect(request.getContextPath() + "/cart?message=Cart item removed successfully");
    }
}
