package com.example.stuff.services.impl;

import com.example.stuff.Entity.Cart;
import com.example.stuff.pojo.CartPojo;
import com.example.stuff.repo.CartRepo;
import com.example.stuff.services.CartServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartServices {
    private final CartRepo cartRepo;


    @Override
    public CartPojo save(CartPojo cartPojo) throws IOException {
        Cart cart;

        cart = new Cart();
        cart.setProduct(cartPojo.getProduct());
        cart.setCartId(cartPojo.getCartId());
        cart.setUser(cartPojo.getUser());

        cartRepo.save(cart);


        return new CartPojo(cart);
    }

    @Override
    public void deleteById(Integer id) {
        cartRepo.deleteById(id);
    }

    @Override
    public List<Cart> findCartItemsByUserId(Integer userId) {
        return null;
    }

    @Override
    public Cart fetchById(Integer id) {
        return cartRepo.findById(id).orElseThrow(()->new RuntimeException("Not Found"));
    }
}
