package com.example.stuff;


import com.example.stuff.Entity.Cart;
import com.example.stuff.Entity.Product;
import com.example.stuff.Entity.User;
import com.example.stuff.repo.CartRepo;
import com.example.stuff.repo.ProductRepo;
import com.example.stuff.repo.UserRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartRepositoryTest {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveCartTest(){
        User user = userRepo.findByEmail2("thapashristy110@gmail.com");
        Product product = productRepo.getReferenceById(33);
        Cart cart = new Cart().builder()
                .product(product)
                .user(user)
                .build();
        cartRepo.save(cart);
        Assertions.assertThat(cart.getCartId()).isGreaterThan(0);
    }


    @Test
    @Order(2)
    @Rollback(value = false)
    public void deleteCartTest(){
        Cart cart = cartRepo.findById(31).get();
        cartRepo.delete(cart);
        Cart cart1 = null;
        Optional<Cart> optionalCart= cartRepo.findById(23);
        if(optionalCart.isPresent()){
            cart1=optionalCart.get();
        }
        Assertions.assertThat(cart1).isNull();
    }
}
