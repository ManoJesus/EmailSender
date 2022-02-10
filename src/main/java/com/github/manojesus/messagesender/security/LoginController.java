package com.github.manojesus.messagesender.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String user(@AuthenticationPrincipal OAuth2User auth2User){
        System.out.println(auth2User);
        System.out.println((String)auth2User.getAttribute("login"));
        return auth2User.getAttribute("name");
    }
}
