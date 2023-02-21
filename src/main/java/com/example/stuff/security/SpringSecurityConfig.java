package com.example.stuff.security;

import com.example.stuff.Entity.EmailCredentials;
import com.example.stuff.config.PasswordEncoderUtil;
import com.example.stuff.repo.EmailCredRepo;
import com.example.stuff.services.impl.CustomerUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Properties;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig  {
    @Autowired
    private EmailCredRepo emailCredRepo;

    private final CustomerUserDetailService customUserDetailService;

    public SpringSecurityConfig(CustomerUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailService);
        authenticationProvider.setPasswordEncoder(PasswordEncoderUtil.getInstance());
        return authenticationProvider;
    }


    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/**")
                .permitAll()
                .requestMatchers("/admin/**")
                .hasAuthority("ADMIN")
                .requestMatchers("/customer/**","/user/**")
                .hasAuthority("CUSTOMER")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .permitAll()
                .defaultSuccessUrl("/admin/Home",true)

                .permitAll().and().httpBasic();
        return httpSecurity.build();


    }

    @Bean
    public JavaMailSender getJavaMailSender() throws Exception {
        try {
            EmailCredentials emailCredentials = emailCredRepo.findOneByActive();
            if (emailCredentials !=null){
                JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

                Properties props = new Properties();
                props.put("mail.transport.protocol", emailCredentials.getProtocol());
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.debug", "true");
                mailSender.setJavaMailProperties(props);

                mailSender.setHost(emailCredentials.getHost());
                mailSender.setPort(Integer.parseInt(emailCredentials.getPort()));
                mailSender.setUsername(emailCredentials.getEmail());
                mailSender.setPassword(emailCredentials.getPassword());
                return mailSender;
            }else {
                return new JavaMailSenderImpl();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring().requestMatchers("/css/**");
    }
}
