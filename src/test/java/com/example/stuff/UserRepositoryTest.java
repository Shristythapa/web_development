package com.example.stuff;

import com.example.stuff.Entity.User;
import com.example.stuff.repo.UserRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepo userRepo;

    @Test
    @Order(1)
    public void saveUserTest(){
        //to use user.builder @builder annotation is required in the builder class
        User user = User.builder()
                .user_name("shristy")
                .email("shristy@gmail.com")
                .build();
        userRepo.save(user);
        Assertions.assertThat(user.getId()).isGreaterThan(0);
    }

     @Test
    @Order(2)
    public void getUserTest(){
        User userCreated = userRepo.findById(21).get();
        Assertions.assertThat(userCreated.getId()).isEqualTo(21);
     }





}
