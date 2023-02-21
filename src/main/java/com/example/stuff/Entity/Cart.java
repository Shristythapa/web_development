package com.example.stuff.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart")
public class Cart {

    @Id
    @SequenceGenerator(name = "cart_seq_gen", sequenceName = "cart_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "cart_seq_gen", strategy = GenerationType.SEQUENCE)
    Integer cartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}
