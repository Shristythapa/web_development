package com.example.stuff;


import com.example.stuff.Entity.Product;
import com.example.stuff.Entity.User;
import com.example.stuff.repo.ProductRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepo productRepo;

    @Test
    @Order(1)
    public void saveProduct(){

      Product product= new Product().builder()
              .productName("product")
              .productPrice(12)
              .image("myimage")
              .build();
      productRepo.save(product);


        Assertions.assertThat(product.getId()).isGreaterThan(0);
        System.out.println(product.getId());
    }

    @Test
    @Order(2)
    public void getById(){
        Product product = productRepo.findById(33).get();
        Assertions.assertThat(product.getId()).isEqualTo(33);
    }
    @Test
    @Order(3)
    public void getAll(){
        List<Product> products = productRepo.findAll();
        Assertions.assertThat(products.size()).isLessThan(0);
    }


    @Test
    @Order(4)
    public void updateProduct(){
        Product product = productRepo.findById(33).get();
        product.setProductName("newname");
        Product productUpdated = productRepo.save(product);
        Assertions.assertThat(productUpdated.getProductName()).isEqualTo("oldname");
    }



    @Test
    @Order(5)
    @Rollback(value = false)
    public void deleteProduct(){
        Product product = productRepo.findById(43).get();
        productRepo.delete(product);
        Product product1 = null;
        Optional<Product> optionalProduct = productRepo.findById(43);
        if(optionalProduct.isPresent()){
            product1=optionalProduct.get();
        }
        Assertions.assertThat(product1).isNull();
    }

}
