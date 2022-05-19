package com.es.phoneshop.model.product;

import com.es.phoneshop.model.GenericDao;
import com.es.phoneshop.model.SortField;
import com.es.phoneshop.model.SortOrder;

import java.util.List;

public interface ProductDao extends GenericDao<Product, Long> {
    List<Product> getSortedProducts(String query, SortField sortField, SortOrder sortOrder);
}
