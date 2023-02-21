package com.example.stuff.services;

import com.example.stuff.Entity.Product;
import com.example.stuff.pojo.ProductPojo;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    ProductPojo save(ProductPojo productPojo) throws IOException;
    List<Product> fetchAll();

    Product fetchById(Integer id);
//    void buy(Integer userId, Integer productId);
    void delete(Integer id);
    List<Product> fetchBySearch(String keyword);
}
