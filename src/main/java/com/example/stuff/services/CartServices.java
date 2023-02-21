package com.example.stuff.services;

import com.example.stuff.Entity.Cart;
import com.example.stuff.pojo.CartPojo;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CartServices {
    CartPojo save(CartPojo cartPojo) throws IOException;
    void deleteById(Integer id);
    List<Cart> findCartItemsByUserId(Integer userId);
    Cart fetchById(Integer id);
}
