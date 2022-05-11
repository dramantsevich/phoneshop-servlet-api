package com.es.phoneshop.model;

public abstract class GenericDao <T, K> {
    public abstract T getItem(K id);
    public abstract void save(T item);
}
