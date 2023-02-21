package com.example.stuff.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Builder
@Getter  //generate getter of all the variables
@Setter //generate setter of all the variables
@NoArgsConstructor  //generate no args constructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @SequenceGenerator(name = "orders_seq_gen", sequenceName = "orders_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "orders_seq_gen", strategy = GenerationType.SEQUENCE)
    Integer orderId;


    @ManyToOne( fetch = FetchType.LAZY)//many orders are related to one user
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")//many orders are related to one product
    private Product product;

    private Integer status;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
