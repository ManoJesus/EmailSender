package com.github.manojesus.messagesender.controller;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.github.manojesus.messagesender.model.FolderByUser;
import com.github.manojesus.messagesender.model.Message;
import com.github.manojesus.messagesender.model.MessageForm;
import com.github.manojesus.messagesender.repository.MessageRepository;
import com.github.manojesus.messagesender.service.FolderByUserService;
import com.github.manojesus.messagesender.service.MessageService;
import com.github.manojesus.messagesender.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/home/compose")
@AllArgsConstructor
public class ComposeEmailController {

    private UserService userService;
    private FolderByUserService folderByUserService;
    private MessageService messageService;

    @GetMapping
    public String getComposePage(@RequestParam(required = false) String to,
                                 @RequestParam(required = false) final String subject,
                                 Model model, @AuthenticationPrincipal OAuth2User auth2User, Principal principal){
        loadTemplateWithEmails(auth2User,principal, model);

        String toList = "";
        if(StringUtils.hasText(to)){
            toList = Stream.of(to.split(","))
                    .map(StringUtils::trimWhitespace)
                    .filter(StringUtils::hasText)
                    .distinct()
                    .collect(Collectors.joining(", "));
        }
        model.addAttribute("messageForm", MessageForm.builder()
                                                        .to(toList)
                                                        .subject(subject).build()) ;
        return "compose-message";
    }
    @PostMapping
    public String sentMessage(@ModelAttribute MessageForm messageForm,@AuthenticationPrincipal OAuth2User auth2User, Principal principal){
        String userName = userService.getUserId(auth2User, principal).toLowerCase();
        Message messageToBeSaved = Message.builder()
                .messageId(Uuids.timeBased())
                .from(userName)
                .to(Stream.of(messageForm.getTo().split(","))
                        .map(StringUtils::trimWhitespace)
                        .filter(StringUtils::hasText)
                        .distinct()
                        .collect(Collectors.toList()))
                .body(messageForm.getBody())
                .subject(messageForm.getSubject()).build();
        messageService.sentEmail(messageToBeSaved);

        return "redirect:/home";
    }





    private void loadTemplateWithEmails(OAuth2User oauthPrincipal, Principal principal, Model model) {
        String userName = userService.getUserId(oauthPrincipal, principal);

        List<FolderByUser> defaultFolders = folderByUserService.createDefaultFolders(userName);
        List<FolderByUser> userFolders = folderByUserService.findAllFolderCreatedByUsers(userName);

        model.addAttribute("userName", userName);
        model.addAttribute("defaultFolders", defaultFolders);
        model.addAttribute("folders", userFolders);
    }
}
