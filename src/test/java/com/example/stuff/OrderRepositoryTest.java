package com.example.stuff;

import com.example.stuff.Entity.Product;
import com.example.stuff.Entity.Order;
import com.example.stuff.Entity.User;
import com.example.stuff.repo.OrderRepo;
import com.example.stuff.repo.ProductRepo;
import com.example.stuff.repo.UserRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;


    @Test
    @Rollback(value = false)
    public void saveTest(){
        User user = userRepo.findByEmail2("thapashristy110@gmail.com");
        Product product = productRepo.getReferenceById(33);
        Order order = new Order().builder()

                .product(product)
                .user(user)
                .build();
        orderRepo.save(order);
        Assertions.assertThat(order.getOrderId()).isGreaterThan(0);
    }



}
