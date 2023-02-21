package com.example.stuff.pojo;


import com.example.stuff.Entity.User;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPojo {

    @Id
    private Integer id;

    @NotNull(message = "email cant be empty")
    private String email;

    @NotNull(message = "username cant be empty")
    private String username;

    @NotNull(message = "password cant be empty")
    private String password;



//    @NotNull
//    private Integer userTypeId;




    public UserPojo(User user){
        this.id=user.getId();
        this.email=user.getEmail();
        this.username=user.getUser_name();
        this.password=user.getPassword();


    }
}
