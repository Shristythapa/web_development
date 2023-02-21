package com.example.stuff.Entity;

import jakarta.persistence.*;

import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Getter  //generate getter of all the variables
@Setter //generate setter of all the variables
@NoArgsConstructor  //generate no args constructor
@AllArgsConstructor  //generate all args constructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @SequenceGenerator(name = "products_seq_gen", sequenceName = "products_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "products_seq_gen", strategy = GenerationType.SEQUENCE)
    Integer id;
    @Column(name = "name")
    String productName;

     @Column(name = "price")
    Integer productPrice;






    private String image;

    @Transient
    private String imageBase64;

    @OneToMany(mappedBy = "product", orphanRemoval = true, cascade = CascadeType.PERSIST)
    List<Order> orders = new ArrayList<Order>();

    @OneToMany(mappedBy = "product", orphanRemoval = true, cascade = CascadeType.PERSIST)
    List<Cart> carts = new ArrayList<Cart>();


}
