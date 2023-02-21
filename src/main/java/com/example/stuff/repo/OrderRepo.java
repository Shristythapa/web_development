package com.example.stuff.repo;

import com.example.stuff.Entity.Order;
import com.example.stuff.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Integer> {

    @Query(value = "select * from orders where user_id =?1", nativeQuery=true)
    List<Order> findOrderByUserId(Integer userId);
}
