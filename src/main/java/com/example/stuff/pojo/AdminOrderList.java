package com.example.stuff.pojo;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderList {
    Integer orderId;
   String customerName;
    String customerEmail;
    String productName;
    Integer productPrice;
    Integer status;
}
