package com.es.phoneshop.model.product;

import com.es.phoneshop.model.SortField;
import com.es.phoneshop.model.SortOrder;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Comparator;

public class DefaultProductService {
    private static final DefaultProductService INSTANCE = new DefaultProductService();

    private DefaultProductService() {
    }

    public static DefaultProductService getInstance() {
        return INSTANCE;
    }

    protected Comparator<Product> getComparator(String query, SortField sortField, SortOrder sortOrder) {
        Comparator<Product> comparator;

        if (sortField == SortField.DESCRIPTION) {
            comparator = Comparator.comparing(Product::getDescription);
        } else if (sortField == SortField.PRICE) {
            comparator = Comparator.comparing(Product::getPrice);
        } else {
            comparator = Comparator.comparing(p -> countWordsAmount(query, p), Comparator.reverseOrder());
        }

        if (sortOrder == SortOrder.DESC) {
            comparator = comparator.reversed();
        }

        return comparator;
    }

    protected long countWordsAmount(String query, Product product) {
        if (StringUtils.isBlank(query)) {
            return 0;
        } else {
            return Arrays.stream(query.split(" "))
                    .filter(product.getDescription()::contains)
                    .count();
        }
    }

    public Long parseProductId(HttpServletRequest request) {
        String productInfo = request.getPathInfo().substring(1);
        return Long.valueOf(productInfo);
    }
}
