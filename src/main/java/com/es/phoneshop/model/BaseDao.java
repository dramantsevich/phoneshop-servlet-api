package com.es.phoneshop.model;

import com.es.phoneshop.model.product.ProductNotFoundException;

import java.util.List;

public interface BaseDao <K, T extends Entity>{
    T findEntityById(K id) throws ProductNotFoundException;
    List<T> findAll();
    void delete(T t);
    void deleteById(K id) throws ProductNotFoundException;
    void create(T t);
}
