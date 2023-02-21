package com.example.stuff.controller;

import com.example.stuff.Entity.Cart;
import com.example.stuff.Entity.Order;
import com.example.stuff.Entity.Product;
import com.example.stuff.Entity.User;
import com.example.stuff.pojo.*;
import com.example.stuff.repo.CartRepo;
import com.example.stuff.repo.OrderRepo;
import com.example.stuff.repo.ProductRepo;
import com.example.stuff.repo.UserRepo;
import com.example.stuff.services.CartServices;
import com.example.stuff.services.OrderServices;
import com.example.stuff.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.util.Base64;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {
    private final ProductService productService;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final OrderServices orderServices;
    private final OrderRepo orderRepo;
    private final CartRepo cartRepo;
    private final CartServices cartServices;
    @GetMapping("/Home")
    public String getCustomerPage(Model model, Principal principal ,@ModelAttribute("keyword") String keyword) {

        if(keyword!=null){
            List<Product> list = productService.fetchBySearch(keyword);
            model.addAttribute("productList",list.stream().map(product ->
                    Product.builder()
                            .id(product.getId())
                            .imageBase64(getImageBase64(product.getImage()))
                            .productName(product.getProductName())
                            .productPrice(product.getProductPrice())

                            .build()
                    ));
        }else{
            List<Product> productList = productService.fetchAll();
            model.addAttribute("product",new ProductPojo());
            System.out.println(productList.get(0).getId());
            model.addAttribute("productList",productList.stream().map(product ->
                    Product.builder()
                            .id(product.getId())
                            .imageBase64(getImageBase64(product.getImage()))
                            .productName(product.getProductName())
                            .productPrice(product.getProductPrice())

                            .build()
            ));
        }

        model.addAttribute("keyword",new String());
        User user = userRepo.findByEmail2(principal.getName());
        Integer userId= user.getId();
        List<Order> orders = orderRepo.findOrderByUserId(userId);
        model.addAttribute("orderList",orders.stream().map(order->
                        CustomerOrderList.builder()
                                .orderId(order.getOrderId())
                                .productName(order.getProduct().getProductName())
                                .productPrice(order.getProduct().getProductPrice())
                                .status(order.getStatus())
                                .build()
                ));

        List<Cart> carts = cartRepo.findOrderByUserId(userId);
        model.addAttribute("cartList",carts.stream().map(cart->
                CartItems.builder()
                        .productId(cart.getProduct().getId())
                        .imageBase64(getImageBase64(cart.getProduct().getImage()))
                        .productName(cart.getProduct().getProductName())
                        .productPrice(cart.getProduct().getProductPrice())
                        .id(cart.getCartId())
                        .build()
        ));

        return "customerDashboard";
    }

    @GetMapping("/search")
    public String searchBar(RedirectAttributes redirectAttributes,@RequestParam("keyword") String keyword){
        if(keyword!=null) {
            List<Product> list = productService.fetchBySearch(keyword);
            redirectAttributes.addFlashAttribute("keyword", keyword);
        }
        return "redirect:/customer/Home";

    }

   @GetMapping("/deleteCart/{id}")
   public String deleteOrder(@PathVariable("id") Integer id,  RedirectAttributes redirectAttributes) throws IOException{
       System.out.println("deleted");
        cartServices.deleteById(id);
       redirectAttributes.addFlashAttribute("deleteMsg", "Order deleted Successfully");
        return "redirect:/customer/Home";

   }

   @GetMapping("/addToCart/{id}")
   public String addToCart(@PathVariable("id") Integer id,Principal principal) throws IOException{
       System.out.println("reachdeddddd");
        Product product = productService.fetchById(id);

       User existingUser = userRepo.findByEmail2(principal.getName());


       CartPojo cartPojo = new CartPojo();
       cartPojo.setProduct(product);
       cartPojo.setUser(existingUser);
       cartServices.save(cartPojo);

       return "redirect:/customer/Home";

   }

    @GetMapping("/buy/{id}")
    public String buyProduct(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes,  Principal principal) throws IOException {

        User existingUser = userRepo.findByEmail2(principal.getName());
       Product boughtProduct = productRepo.getReferenceById(id);

        //creating order or the user in order table
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setProduct(boughtProduct);
        orderPojo.setUser(existingUser);
        orderPojo.setStatus(1);


        orderServices.save(orderPojo);


        //  redirectAttributes.addFlashAttribute("successMsg", "User saved successfully");
        return "redirect:/customer/Home";
    }


    @GetMapping("/buyCart/{id}")
    public String buy(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes,  Principal principal) throws IOException {
        Cart cart = cartServices.fetchById(id);

        Product product=cart.getProduct();
        //fetching product and user data
        CartPojo cartPojo = new CartPojo(cart);
        Integer cartId=cartPojo.getCartId();
       cartServices.deleteById(cartId);
        User existingUser = userRepo.findByEmail2(principal.getName());
//        Product boughtProduct = productRepo.getReferenceById(product.getId());

        //creating order or the user in order table
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setProduct(product);
        orderPojo.setUser(existingUser);
        orderPojo.setStatus(1);


        orderServices.save(orderPojo);
        productRepo.save(product);

      //  redirectAttributes.addFlashAttribute("successMsg", "User saved successfully");
        return "redirect:/customer/Home";
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
