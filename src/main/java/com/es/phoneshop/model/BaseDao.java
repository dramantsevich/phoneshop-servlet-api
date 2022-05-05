package com.es.phoneshop.model;

import com.es.phoneshop.model.product.ProductNotFoundException;

import java.util.List;

public interface BaseDao <K, T extends Entity>{
    T findProductById(K id) throws ProductNotFoundException;
    List<T> findAllProducts();
    void delete(T t);
    void deleteById(K id) throws ProductNotFoundException;
    void create(T t);
}
