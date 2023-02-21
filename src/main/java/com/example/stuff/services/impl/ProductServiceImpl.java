package com.example.stuff.services.impl;

import com.example.stuff.Entity.Product;
import com.example.stuff.pojo.ProductPojo;
import com.example.stuff.repo.ProductRepo;
import com.example.stuff.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/productImages";

    @Override
    public ProductPojo save(ProductPojo productPojo) throws IOException {
        Product product= new Product();
        product.setId(productPojo.getId());
       product.setProductName(productPojo.getProductName());

       product.setProductPrice(productPojo.getProductPrice());

        if(productPojo.getImage()!=null){ /// to upload file the file be saved in the server folder and the name of the file will be saved in the database
            StringBuilder fileNames = new StringBuilder();
            System.out.println(UPLOAD_DIRECTORY);
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, productPojo.getImage().getOriginalFilename());
            fileNames.append(productPojo.getImage().getOriginalFilename());
            Files.write(fileNameAndPath, productPojo.getImage().getBytes()); //front bata pathako files lai specified name ma rakhnai

            product.setImage(productPojo.getImage().getOriginalFilename());
        }

        productRepo.save(product);
        return new ProductPojo(product);
    }

    @Override
    public List<Product> fetchAll() {
       return productRepo.findAll();
    }

    public Product fetchById(Integer id) {
        return productRepo.findById(id).orElseThrow(()->new RuntimeException("Not Found"));
    }


    @Override
    public void delete(Integer id) {
          productRepo.deleteById(id);
    }

    @Override
    public List<Product> fetchBySearch(String keyword) {
        return productRepo.findByKeyword(keyword);
    }


}
