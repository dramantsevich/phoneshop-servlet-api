package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.GenericDao;
import com.es.phoneshop.model.SortField;
import com.es.phoneshop.model.SortOrder;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

public class ArrayListProductDao extends GenericDao<Product, Long> {
    private static ArrayListProductDao instance;

    public static synchronized ArrayListProductDao getInstance() throws ProductNotFoundException {
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

    public synchronized List<Product> findProducts(String query) {
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

    public synchronized List<Product> findSortedProducts(SortField sortField, SortOrder sortOrder) {
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
    public Product getItemById(Long id) {
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
    public Product getItem(Long id) {
        return null;
    }

    //    @Override
//    public synchronized Product findProductById(Long id) throws ProductNotFoundException {
//        return products.stream()
//                .filter(product -> id.equals(product.getId()))
//                .findAny()
//                .orElseThrow(ProductNotFoundException::new);
//    }
//
//    @Override
//    public synchronized void create(Product product) {
//        if(product.getId() != null){
//            products.set(product.getId().intValue(), product);
//        }else{
//            product.setId(maxId++);
//            products.add(product);
//        }
//    }

    public synchronized void delete(Product product) {
        if(product != null)
            products.remove(product);
    }

    public synchronized void deleteById(Long id) throws ProductNotFoundException {
        if(id >= 0){
            products.remove(products.stream()
                    .filter(p -> Objects.equals(p.getId(), id))
                    .findAny()
                    .orElseThrow(ProductNotFoundException::new));
        }
    }

    public synchronized List<Product> findRecentlyViewedProducts(Deque<Long> recentlyViewedProductsId) {
        Set<Product> productList = new HashSet<>();

        for(Product product : products) {
            Long productId = product.getId();

            for(Long productViewedId : recentlyViewedProductsId) {
                if(productId.equals(productViewedId)) {
                    productList.add(product);
                }
            }
        }

        return new ArrayList<>(productList);
    }

}
