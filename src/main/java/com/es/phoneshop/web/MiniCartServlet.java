package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.ArrayListCartDao;
import com.es.phoneshop.model.cart.CartDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MiniCartServlet extends HttpServlet {
    private CartDao cartDao;

    @Override
    public void init(ServletConfig config) {
        cartDao = ArrayListCartDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("cart", cartDao.getCart(request));
        request.getRequestDispatcher("/WEB-INF/pages/minicart.jsp").include(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
