package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.*;
import com.es.phoneshop.model.product.*;
import com.es.phoneshop.model.history.DefaultViewHistoryService;
import com.es.phoneshop.model.history.ViewHistory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartDao cartDao;
    private DefaultProductService productService;
    private DefaultViewHistoryService viewHistoryService;

    @Override
    public void init(ServletConfig config) {
        productDao = ArrayListProductDao.getInstance();
        cartDao = ArrayListCartDao.getInstance();
        productService = DefaultProductService.getInstance();
        viewHistoryService = DefaultViewHistoryService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Product product = getProductIfExist(request,response);
        Cart cart = cartDao.getCart(request);
        ViewHistory viewHistory = viewHistoryService.getViewHistory(request);
        viewHistoryService.add(viewHistory, product);

        request.setAttribute("product", product);
        request.setAttribute("cart", cart);
        request.setAttribute("viewedProducts", viewHistory);

        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Long productId = productService.parseProductId(request);
        String quantityString = request.getParameter("quantity");

        int quantity;
        try {
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(quantityString).intValue();
        } catch (ParseException ex) {
            request.setAttribute("error", "Not a number");
            doGet(request, response);
            return;
        }

        Cart cart = cartDao.getCart(request);
        try {
            cartDao.add(cart, productId, quantity);
        } catch (OutOfStockException e) {
            HandleError.handleProductDetailError(request, response, e);
            doGet(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/products/" + productId + "?message= Product added to cart");
    }

    private Product getProductIfExist(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Product product = null;

        try{
            Long id = productService.parseProductId(request);
            product = productDao.getItem(id);
        } catch (ProductNotFoundException | NumberFormatException e) {
            HandleError.handleProductDetailError(request, response, e);
        }
        return product;
    }
}
