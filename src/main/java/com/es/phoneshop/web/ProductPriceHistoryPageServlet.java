package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ProductNotFoundException;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductPriceHistoryPageServlet extends HttpServlet {
    private ArrayListProductDao productDao;

    @Override
    public void init(ServletConfig config) throws  ServletException{
        super.init(config);
        try {
            productDao = ArrayListProductDao.getInstance();
        } catch (ProductNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getPathInfo();
        request.setAttribute("product", productDao.getItemById(Long.valueOf(productId.substring(1))));
        request.getRequestDispatcher("/WEB-INF/pages/productPriceHistory.jsp").forward(request, response);
    }
}
