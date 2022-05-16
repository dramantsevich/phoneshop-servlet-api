package com.es.phoneshop.model.history;

import com.es.phoneshop.model.product.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public class DefaultViewHistoryService {
    private static final String VIEW_HISTORY_SESSION_ATTRIBUTE = DefaultViewHistoryService.class.getName() + ".history";
    private static final DefaultViewHistoryService INSTANCE = new DefaultViewHistoryService();

    private DefaultViewHistoryService() {
    }

    public static DefaultViewHistoryService getInstance() {
        return INSTANCE;
    }

    public ViewHistory getViewHistory(HttpServletRequest request) {
        ViewHistory viewHistory = (ViewHistory) request.getSession().getAttribute(VIEW_HISTORY_SESSION_ATTRIBUTE);

        if (viewHistory == null) {
            request.getSession().setAttribute(VIEW_HISTORY_SESSION_ATTRIBUTE, viewHistory = new ViewHistory());
        }

        return viewHistory;
    }

    public synchronized void add(ViewHistory viewHistory, Product product) {
        List<Product> viewHistoryList = viewHistory.getItems();

        Optional<Product> optionalProduct = viewHistoryList.stream()
                .filter(p -> p.getId().equals(product.getId()))
                .findAny();

        if(!optionalProduct.isPresent()){
            if(viewHistoryList.size() < 3){
                viewHistoryList.add(product);
            } else {
                viewHistoryList.remove(0);
                viewHistoryList.add(viewHistoryList.size(), product);
            }
        }
    }
}
