package com.github.manojesus.messagesender.controller;

import com.github.manojesus.messagesender.model.EmailByUserFolder;
import com.github.manojesus.messagesender.model.FolderByUser;
import com.github.manojesus.messagesender.model.Message;
import com.github.manojesus.messagesender.repository.MessageRepository;
import com.github.manojesus.messagesender.service.FolderByUserService;
import com.github.manojesus.messagesender.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class MessageViewController {

    private final UserService userService;
    private final MessageRepository messageRepository;
    private final FolderByUserService folderByUserService;

    @GetMapping("/home/message/{messageId}")
    public String messageView(@AuthenticationPrincipal OAuth2User oauthPrincipal,
                              Principal principal,
                              Model model,
                              @PathVariable UUID messageId){
        loadTemplateWithEmails(oauthPrincipal, principal, model);

        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if(optionalMessage.isEmpty()){
            return "redirect:/home";
        }
        Message message = optionalMessage.get();
        String listTo = String.join(", ",message.getTo());

        model.addAttribute("message", message);
        model.addAttribute("listTo", listTo);

        return "message-view";
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
