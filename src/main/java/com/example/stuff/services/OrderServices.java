package com.example.stuff.services;

import com.example.stuff.Entity.Order;
import com.example.stuff.Entity.Product;
import com.example.stuff.pojo.OrderPojo;

import java.io.IOException;
import java.util.List;

public interface OrderServices {
    OrderPojo save(OrderPojo orderPojo) throws IOException;
    List<Order> fetchAll();
    Order findById(Integer userId);
    void delete(Integer orderId);

}
