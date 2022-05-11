package com.es.phoneshop.web;

import com.es.phoneshop.model.SortField;
import com.es.phoneshop.model.SortOrder;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductNotFoundException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {
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
        String query = request.getParameter("query");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");

        if(query != null && !query.equals("")){
            request.setAttribute("products", productDao.findProducts(query));
        } else {
            request.setAttribute("products", productDao.findSortedProducts(
                    Optional.ofNullable(sortField).map(SortField::valueOf).orElse(null),
                    Optional.ofNullable(sortOrder).map(SortOrder::valueOf).orElse(null)
            ));
        }

        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }
}
