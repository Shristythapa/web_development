package com.example.stuff.services.impl;

import com.example.stuff.Entity.Order;
import com.example.stuff.pojo.OrderPojo;
import com.example.stuff.repo.OrderRepo;
import com.example.stuff.services.OrderServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderServices {

    private final OrderRepo orderRepo;

    @Override
    public OrderPojo save(OrderPojo orderPojo) throws IOException {
        Order order;

        order = new Order();
        order.setOrderId(orderPojo.getOrderId());
        order.setProduct(orderPojo.getProduct());
        order.setUser(orderPojo.getUser());
        order.setStatus(orderPojo.getStatus());

        orderRepo.save(order);


        return new OrderPojo(order);
    }

    @Override
    public List<Order> fetchAll() {
        return orderRepo.findAll();
    }

    @Override
    public Order findById(Integer orderId) {
        return orderRepo.findById(orderId).orElseThrow(()->new RuntimeException("Not Found"));

    }

    @Override
    public void delete(Integer orderId) {
         orderRepo.deleteById(orderId);
    }


}
