package com.es.phoneshop.model;

public interface GenericDao<T, K> {
    T getItem(K id);

    void save(T item);
}
