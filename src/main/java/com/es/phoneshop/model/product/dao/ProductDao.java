package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.BaseDao;
import com.es.phoneshop.model.SortField;
import com.es.phoneshop.model.SortOrder;
import com.es.phoneshop.model.product.Product;

import java.util.List;

public interface ProductDao extends BaseDao<Long, Product> {
    List<Product> findProducts(String query);
    List<Product> findSortedProducts(SortField sortField, SortOrder sortOrder);
}
