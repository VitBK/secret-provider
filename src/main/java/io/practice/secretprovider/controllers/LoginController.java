package io.practice.secretprovider.controllers;

import io.practice.secretprovider.model.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @Autowired
    private JdbcUserDetailsManager userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public ModelAndView getRegister(ModelAndView modelAndView) {
        modelAndView.addObject("user", new RegistrationRequest());
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @PostMapping(path = "/register", consumes = "application/x-www-form-urlencoded")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerUser(@ModelAttribute("user") RegistrationRequest request) {
        UserDetails userDetails = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .passwordEncoder(password -> passwordEncoder.encode(password))
                .roles("USER")
                .authorities("STANDARD")
                .build();
        userDetailsService.createUser(userDetails);
        return "login";
    }
}
