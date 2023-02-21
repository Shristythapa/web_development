package com.example.stuff.services.impl;


import com.example.stuff.Entity.User;
import com.example.stuff.config.PasswordEncoderUtil;
import com.example.stuff.pojo.UserPojo;

import com.example.stuff.repo.UserRepo;
import com.example.stuff.services.UserService;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;

    @Override
    public  UserPojo save(UserPojo userPojo) throws IOException {
        User user;
        if (userPojo.getId() != null) {
            user = userRepo.findById(userPojo.getId()).orElseThrow(() -> new RuntimeException("Not Found"));
        } else {
            user = new User();
        }
        user.setEmail(userPojo.getEmail());
        user.setUser_name(userPojo.getUsername());
        user.setPassword(PasswordEncoderUtil.getInstance().encode(userPojo.getPassword()));

        userRepo.save(user);

        userRepo.insertUserRole(user.getId());
        return new UserPojo(user);
    }

    @Override
    public boolean isUserPresent(String email) {
        boolean userExist = false;
        String message=null;
        Optional<User> existingUser = userRepo.findByEmail(email);
        if(existingUser.isPresent()){
            userExist=true;
            message="Email Already Present";
        }
        return userExist;
    }


    @Override
    public void sendEmail( String email,int myMessage) {
        try {


            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("smtp.gmail.com");
            mailSender.setPort(465);

            String host="smtp.gmail.com";
            Properties properties = System.getProperties();
            System.out.println("properties"+properties);
            properties.put("mail.smtp.host",host);
            properties.put("mail.smtp.port","465");
            properties.put("mail.smtp.ssl.enable","true");
            properties.put("mail.smtp.auth","true");

            //get session obj
            Session session =Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("stuffecommerce123@gmail.com","qfdgxjqzqjmnqciy");
                }
            });
            MimeMessage message = new MimeMessage(session);
            message.addRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(email)});
            message.setText("your otp code is "+myMessage+" ");
            message.setSubject("Verify Password");
            message.setFrom("stuffecomerce123@gmail.com");
            Transport.send(message);
                 //   getJavaMailSender.send(message);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public User getUserByEmail(String email) {

        return userRepo.findByEmail(email).get();
    }

    @Override
    public void forgotPassword(String username) {

    }

    @Override
    public void updatePassword(String password, String token) {

    }

    @Override
    public void sendResetPasswordEmail(User user) {

    }


}