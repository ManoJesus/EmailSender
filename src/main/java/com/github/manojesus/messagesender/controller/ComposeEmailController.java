package com.github.manojesus.messagesender.controller;

import com.github.manojesus.messagesender.model.MessageForm;
import com.github.manojesus.messagesender.service.FolderByUserService;
import com.github.manojesus.messagesender.service.MessageService;
import com.github.manojesus.messagesender.service.UserService;
import com.github.manojesus.messagesender.util.LoadDefaultModel;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.manojesus.messagesender.util.AttributesNames.MESSAGE_FORM;
import static com.github.manojesus.messagesender.util.UrlNames.COMPOSE_URL;
import static com.github.manojesus.messagesender.util.UrlNames.HOME_URL;
import static com.github.manojesus.messagesender.util.ViewNames.COMPOSE_MESSAGE;

@Controller
@RequestMapping(COMPOSE_URL)
@AllArgsConstructor
public class ComposeEmailController {

    private UserService userService;
    private FolderByUserService folderByUserService;
    private MessageService messageService;

    @GetMapping
    public String getComposePage(@RequestParam(required = false) String to,
                                 @RequestParam(required = false) final String subject,
                                 Model model){
        String toList = "";
        if(StringUtils.hasText(to)){
            toList = Stream.of(to.split(","))
                    .map(StringUtils::trimWhitespace)
                    .filter(StringUtils::hasText)
                    .distinct()
                    .collect(Collectors.joining(", "));
        }
        model.addAttribute(MESSAGE_FORM, MessageForm.builder()
                                                        .to(toList)
                                                        .subject(subject).build()) ;
        return COMPOSE_MESSAGE.getName();
    }
    @PostMapping
    public String sentMessage(@ModelAttribute MessageForm messageForm,@AuthenticationPrincipal OAuth2User auth2User, Principal principal){
        String userName = userService.getUserId(auth2User, principal).toLowerCase();
        messageService.sentEmail(messageForm, userName);
        return "redirect:"+HOME_URL;
    }

    @ModelAttribute
    void loadFoldersTemplate(Model model,@AuthenticationPrincipal OAuth2User oauthPrincipal, Principal principal){
        String username = userService.getUserId(oauthPrincipal,principal);
        LoadDefaultModel.loadTemplateWithEmails(username,model,folderByUserService);
    }
}
