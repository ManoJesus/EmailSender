package com.github.manojesus.messagesender.controller;

import com.github.manojesus.messagesender.model.User;
import com.github.manojesus.messagesender.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class LoginSingUpController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(Model model){
        return "index";
    }
    @GetMapping("/login-failure")
    public String failToLogin(Model model){
        model.addAttribute("errorToLogin", true);
        return "index";
    }
    @GetMapping("/signup")
    public String singUpForm(Model model){
        User userToRegister =User.builder().build();
        model.addAttribute("userToRegister", userToRegister);
        return "signup";
    }
    @PostMapping("/signup")
    public String singUp(@ModelAttribute User userToRegister, Model model){
        model.addAttribute("userToRegister",userToRegister);
        userService.singUp(userToRegister);
        return "redirect:/login";
    }

}
