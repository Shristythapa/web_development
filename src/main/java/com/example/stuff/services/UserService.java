package com.example.stuff.services;

import com.example.stuff.Entity.User;
import com.example.stuff.pojo.UserPojo;

import java.io.IOException;

public interface UserService {

    UserPojo save(UserPojo userPojo) throws IOException;
//    Boolean checkEmail(String email);
 boolean isUserPresent(String email);
//
//    void deleteById(Integer id);
    void sendEmail(String email, int myMessage);
User getUserByEmail(String email);
   void forgotPassword(String username);
    void updatePassword(String password, String token);
    void sendResetPasswordEmail(User user);
}
