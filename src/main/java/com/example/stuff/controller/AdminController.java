package com.example.stuff.controller;

import com.example.stuff.Entity.Order;
import com.example.stuff.Entity.Product;
import com.example.stuff.Entity.Role;
import com.example.stuff.Entity.User;
import com.example.stuff.pojo.AdminOrderList;
import com.example.stuff.pojo.OrderPojo;
import com.example.stuff.pojo.ProductPojo;
import com.example.stuff.pojo.UserPojo;
import com.example.stuff.services.OrderServices;
import com.example.stuff.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final ProductService productService;
    private final OrderServices orderServices;
    @GetMapping("/Home")
    public String getAdminPage(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean hasUserRole = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ADMIN"));
        System.out.println(hasUserRole);
        if(hasUserRole){

            UserPojo userPojo = new UserPojo();
            User myUser=(User)authentication.getPrincipal();
            userPojo.setUsername(myUser.getUser_name());

            model.addAttribute("user",userPojo);

            model.addAttribute("product",new ProductPojo());
            List<Product> productList = productService.fetchAll();
            model.addAttribute("productList",productList.stream().map(product ->
                    Product.builder()
                            .id(product.getId())
                            .imageBase64(getImageBase64(product.getImage()))
                            .productName(product.getProductName())
                            .productPrice(product.getProductPrice())

                            .build()
            ));


            List<Order> orderList = orderServices.fetchAll();
            model.addAttribute("order",new OrderPojo());

            model.addAttribute("ord",new AdminOrderList());
            model.addAttribute("orderList",orderList.stream().map(order ->

                    AdminOrderList.builder()
                            .orderId(order.getOrderId())
                            .customerName(order.getUser().getUser_name())
                            .customerEmail(order.getUser().getEmail())
                            .productName(order.getProduct().getProductName())
                            .productPrice(order.getProduct().getProductPrice())
                            .status(order.getStatus())
                            .build()
            ));


            return "adminDashboard";
        }

        return "redirect:/customer/Home";
    }



    @PostMapping("/addProduct")
    public String addProduct(@Valid ProductPojo productPojo, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {
        Map<String, String> requestError = validateRequest(bindingResult);
        if (requestError != null) {
            redirectAttributes.addFlashAttribute("requestError", requestError);
            return "redirect:admin/Home";
        }
        productService.save(productPojo);
        redirectAttributes.addFlashAttribute("successMsg", "User saved successfully");

        return "redirect:/admin/Home";

    }

    @GetMapping("/updateProduct/{id}")
    public String editProduct(@PathVariable("id") Integer id, Model model){
        Product myProduct= productService.fetchById(id);
        model.addAttribute("product",myProduct);
        return "updatePage";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id){
        System.out.println("Delete controller reached");
        Product prouduct = productService.fetchById(id);

        productService.delete(prouduct.getId());
        return "redirect:/admin/Home";
    }
    @PostMapping("/orderUpdate/{id}")
    public String updateOrderStatus(@PathVariable("id") Integer id, OrderPojo orderPojo) throws IOException {
        System.out.println("update reached");
        Order order =orderServices.findById(id);
        order.setStatus(orderPojo.getStatus());
        OrderPojo pojo = new OrderPojo(order);
        orderServices.save(pojo);
        return "redirect:/admin/Home";
    }

    public Map<String, String> validateRequest(BindingResult bindingResult) {
        System.out.println(bindingResult);
        if (!bindingResult.hasErrors()) {
            return null;
        }
        Map<String, String> errors = new HashMap<>();
        bindingResult.getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return errors;

    }

    public String getImageBase64(String fileName) {
        String filePath = System.getProperty("user.dir") + "/productImages/";
        File file = new File(filePath + fileName);
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        String base64 = Base64.getEncoder().encodeToString(bytes);
        return base64;
    }


}
