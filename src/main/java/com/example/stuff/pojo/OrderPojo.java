package com.example.stuff.pojo;

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
public class OrderPojo {



    @Id
    private Integer orderId;

    @NotNull(message = "product id cant be null")
    private Product product ;

    @NotNull(message = "status of order is required")
    private  Integer status;

    @NotNull(message = "user id cant be null")
    private User user;

    public OrderPojo(Order order){
        this.orderId=order.getOrderId();
        this.product=order.getProduct();
        this.user=order.getUser();
        this.status=order.getStatus();

    }
}
