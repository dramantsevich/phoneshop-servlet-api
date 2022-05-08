package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.ProductNotFoundException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.Deque;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;
    Deque<Long> recentlyViewedProductsId = new ArrayDeque<>();

    @Override
    public void init(ServletConfig config) throws  ServletException{
        super.init(config);
        try {
            productDao = ArrayListProductDao.getInstance();
            cartService = DefaultCartService.getInstance();
        } catch (ProductNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = parseProductId(request);

        recentlyViewedProductsId = addRecentlyViewedProducts(productId);

        request.setAttribute("product", productDao.findProductById(productId));
        request.setAttribute("cart", cartService.getCart(request));
        request.setAttribute("viewedProducts", productDao.findRecentlyViewedProducts(recentlyViewedProductsId));

        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
    }

    private Deque<Long> addRecentlyViewedProducts(Long productId) {
        if(recentlyViewedProductsId.size() < 3) {
            recentlyViewedProductsId.add(productId);
        } else {
            recentlyViewedProductsId.removeFirst();
            recentlyViewedProductsId.addLast(productId);
        }

        return recentlyViewedProductsId;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = parseProductId(request);
        String quantityString = request.getParameter("quantity");

        int quantity;
        try{
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(quantityString).intValue();
        } catch (ParseException ex) {
            request.setAttribute("error", "Not a number");
            doGet(request, response);
            return;
        }

        Cart cart = cartService.getCart(request);
        try {
            cartService.add(cart, productId, quantity);
        } catch (OutOfStockException e) {
            request.setAttribute("error", "Out of stock, available " + e.getStockAvailable());
            doGet(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/products/" + productId + "?message= Product added to cart");
    }

    private Long parseProductId(HttpServletRequest request) {
        String productInfo = request.getPathInfo().substring(1);
        return Long.valueOf(productInfo);
    }
}
