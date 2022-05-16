package com.es.phoneshop.web;

import com.es.phoneshop.model.SortField;
import com.es.phoneshop.model.SortOrder;
import com.es.phoneshop.model.cart.ArrayListCartDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartDao;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

public class ProductListPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartDao cartDao;

    @Override
    public void init(ServletConfig config) {
        productDao = ArrayListProductDao.getInstance();
        cartDao = ArrayListCartDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");

        if (Objects.nonNull(sortField)) {
            sortField = sortField.toUpperCase();
        }
        if (Objects.nonNull(sortOrder)) {
            sortOrder = sortOrder.toUpperCase();
        }

        request.setAttribute("products",
                productDao.getSortedProducts(query,
                        Optional.ofNullable(sortField).map(SortField::valueOf).orElse(null),
                        Optional.ofNullable(sortOrder).map(SortOrder::valueOf).orElse(null)));

        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");

        Map<Long, String> errors = new HashMap<>();

        for (int i = 0; i < productIds.length; i++) {
            Long productId = Long.valueOf(productIds[i]);
            Cart cart = cartDao.getCart(request);
            NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());
            try {
                int quantity = numberFormat.parse(quantities[i]).intValue();
                if (quantity != 0) {
                    cartDao.update(cart, productId, quantity);
                }
            } catch (ParseException | OutOfStockException e) {
                HandleError.handleProductsError(errors, productId, e);
            }
        }

        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/products?message=Cart updated successfully");
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }
}
