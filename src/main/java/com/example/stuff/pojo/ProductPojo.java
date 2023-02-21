package com.example.stuff.pojo;


import com.example.stuff.Entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPojo {

    @Id
    private Integer id;

    @NotNull(message = "Product name cant be empty")
    private String productName;

    @NotNull(message  ="Product Price cant be empty")
    private Integer productPrice;


    private MultipartFile image;

    public ProductPojo(Product product){
        this.id=product.getId();
        this.productName=product.getProductName();
        this.productPrice=product.getProductPrice();

    }
}
