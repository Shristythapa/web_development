package com.example.stuff.controller;
import com.example.stuff.Entity.User;

import com.example.stuff.config.PasswordEncoderUtil;
import com.example.stuff.pojo.UserPojo;
import com.example.stuff.repo.UserRepo;
import com.example.stuff.services.UserService;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Controller

@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserRepo userRepo;
    private final UserService userService;
    private static final int OTP_LENGTH = 6;
    SecureRandom random = new SecureRandom();
    int otp ;

    @GetMapping("/register")
    public String getRegister(Model model) {
        UserPojo user = new UserPojo();
        model.addAttribute("users", user);//pass user object of user pojo to regester template

        return "signup";
    }




    @PostMapping("/create")
    public String createUser(@Valid UserPojo userPojo,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {

        Map<String, String> requestError = validateRequest(bindingResult);
        System.out.println(requestError);
        if (requestError != null) {
         redirectAttributes.addFlashAttribute("requestError", requestError);
            return "redirect:/user/register";
        }
        userService.save(userPojo);
        redirectAttributes.addFlashAttribute("successMsg", "User saved successfully");
        return "redirect:/login";



    }


    @PostMapping("/logout")
    public String logout(Authentication authentication) {
        if (authentication.isAuthenticated()) {
            SecurityContextHolder.clearContext();
        }
        return "redirect:/login";
    }

    public Map<String, String> validateRequest(BindingResult bindingResult) {
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



    @GetMapping("/forgotPassword")
    public String getForgotPassword() {
        System.out.println("Forgot password");
        return "forgotPassword";
    }

    @PostMapping("/getOTP")
    public String resetPassword(@RequestParam("email") String email, HttpSession session, RedirectAttributes redirectAttributes){
        System.out.println("email::::::"+email);

        otp= random.nextInt((int) Math.pow(10, OTP_LENGTH));
        System.out.println("outotpppppppppppp"+otp);

        if(userService.isUserPresent(email)){
            userService.sendEmail(email,otp);
            session.setAttribute("otp",otp);
            session.setAttribute("email",email);
            session.setAttribute("message","email send");
            return "verifyOTP";
        }else{
            redirectAttributes.addAttribute("emailNotFound","user not found");
            return "redirect:/user/forgotPassword";
        }


    }



    @PostMapping("/verifyOTP")
    public String postVerifyOTP(@RequestParam("otp") int OTP,HttpSession session, @RequestParam(value = "newPassword", required = false) String myPassword ,Model model, RedirectAttributes redirectAttributes) throws IOException {
        Integer myOtp=(int) session.getAttribute("otp");
        String email=(String) session.getAttribute("email");
        if(OTP== myOtp){
            User user = userService.getUserByEmail(email);
            user.setPassword(PasswordEncoderUtil.getInstance().encode(myPassword));
            userRepo.save(user);
            redirectAttributes.addFlashAttribute("otpSuccess","Password Updated");
            System.out.println("Password Changed");
            return "redirect:/login";
          }

        redirectAttributes.addFlashAttribute("otpError","Invalid otp entered");
        System.out.println("Password not changed");
        return "redirect:/login";
    }


}
