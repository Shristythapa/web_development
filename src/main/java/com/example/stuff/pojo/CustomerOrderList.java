package com.example.stuff.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomerOrderList {
    Integer orderId;
    String productName;
    Integer productPrice;
    Integer status;
}
