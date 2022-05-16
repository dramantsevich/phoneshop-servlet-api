package com.es.phoneshop.model.product;

import com.es.phoneshop.model.SortField;
import com.es.phoneshop.model.SortOrder;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private static final ArrayListProductDao INSTANCE = new ArrayListProductDao();
    private final DefaultProductService productService;
    private long maxId;
    private final List<Product> products;

    private ArrayListProductDao() {
        this.products = new ArrayList<>();
        this.productService = DefaultProductService.getInstance();
    }

    public static ArrayListProductDao getInstance() {
        return INSTANCE;
    }

    @Override
    public Product getItem(Long id) {
        return products.stream()
                .filter(product -> id.equals(product.getId()))
                .findAny()
                .orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public void save(Product item) {
        if (item.getId() != null) {
            products.set(item.getId().intValue(), item);
        } else {
            item.setId(maxId++);
            products.add(item);
        }
    }

    @Override
    public List<Product> getSortedProducts(String query, SortField sortField, SortOrder sortOrder) {
        return products.stream()
                .filter(p -> p.getPrice() != null)
                .filter(p -> p.getStock() > 0)
                .filter(p -> StringUtils.isBlank(query) || productService.countWordsAmount(query, p) > 0)
                .sorted(productService.getComparator(query, sortField, sortOrder))
                .collect(Collectors.toList());
    }
}
