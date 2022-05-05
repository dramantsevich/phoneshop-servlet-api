package com.es.phoneshop.model.product.dao;


import com.es.phoneshop.model.SortField;
import com.es.phoneshop.model.SortOrder;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private static ProductDao instance;

    public static synchronized ProductDao getInstance() throws ProductNotFoundException {
        if(instance == null){
            instance = new ArrayListProductDao();
        }
        return instance;
    }

    private long maxId;
    private List<Product> products;

    private ArrayListProductDao() throws ProductNotFoundException {
        this.products = new ArrayList<>();
    }

    @Override
    public List<Product> findProducts(String query) {
        Set<Product> productList = new HashSet<>();
        String [] queryArray = query.toLowerCase().split(" ");

        for(Product product : products) {
            String productDescription = product.getDescription().toLowerCase();
            String [] productDescriptionArray = productDescription.split(" ");

            for(String str : queryArray) {
                for(String productDescriptionStr : productDescriptionArray) {
                    if(productDescriptionStr.equals(str)) {
                        productList.add(product);
                    }
                }
            }
        }

        return new ArrayList<>(productList);
    }

    @Override
    public List<Product> findSortedProducts(SortField sortField, SortOrder sortOrder) {
        Comparator<Product> comparator = sortComparator(sortField);
        if(SortOrder.asc == sortOrder) {
            return products.stream()
                    .filter(p -> p.getPrice() != null)
                    .filter(this::productIsInStock)
                    .sorted(comparator)
                    .collect(Collectors.toList());
        } else {
            return products.stream()
                    .filter(p -> p.getPrice() != null)
                    .filter(this::productIsInStock)
                    .sorted(comparator.reversed())
                    .collect(Collectors.toList());
        }
    }

    private static Comparator<Product> sortComparator(SortField sortField) {
        return Comparator.comparing(product -> {
            if(SortField.description == sortField){
                return (Comparable) product.getDescription();
            } else {
                return (Comparable) product.getPrice();
            }
        });
    }

    @Override
    public synchronized List<Product> findAllProducts() {
        return products.stream()
                .filter(p -> p.getPrice() != null)
                .filter(this::productIsInStock)
                .collect(Collectors.toList());
    }

    private boolean productIsInStock(Product product) {
        return product.getStock() > 0;
    }

    @Override
    public synchronized Product findProductById(Long id) throws ProductNotFoundException {
        return products.stream()
                .filter(product -> id.equals(product.getId()))
                .findAny()
                .orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public synchronized void delete(Product product) {
        if(product != null)
            products.remove(product);
    }

    @Override
    public synchronized void deleteById(Long id) throws ProductNotFoundException {
        if(id >= 0){
            products.remove(products.stream()
                    .filter(p -> Objects.equals(p.getId(), id))
                    .findAny()
                    .orElseThrow(ProductNotFoundException::new));
        }
    }

    @Override
    public synchronized void create(Product product) {
        if(product.getId() != null){
            products.set(product.getId().intValue(), product);
        }else{
            product.setId(maxId++);
            products.add(product);
        }
    }

}
