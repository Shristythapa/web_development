package com.example.stuff.pojo;

import com.example.stuff.Entity.Cart;
import com.example.stuff.Entity.Order;
import com.example.stuff.Entity.Product;
import com.example.stuff.Entity.User;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartPojo {
    @Id
    private Integer cartId;

    @NotNull(message = "product id cant be null")
    private Product product ;



    @NotNull(message = "user id cant be null")
    private User user;

    public CartPojo(Cart cart){
        this.cartId=cart.getCartId();
        this.product=cart.getProduct();
        this.user=cart.getUser();


    }
}
