package com.example.stuff.pojo;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItems {

    Integer productId;
    Integer id;
    private String imageBase64;
    String productName;
    Integer productPrice;

}
