package com.github.manojesus.messagesender.controller;

import com.github.manojesus.messagesender.util.LoadDefaultModel;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;


import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

import static com.github.manojesus.messagesender.util.UrlNames.HOME_URL;
import static com.github.manojesus.messagesender.util.UrlNames.MESSAGE_VIEW_URL;
import static com.github.manojesus.messagesender.util.ViewNames.MESSAGE_VIEW;

@Controller
@AllArgsConstructor
public class MessageViewController {

    private final UserService userService;
    private final MessageRepository messageRepository;
    private final FolderByUserService folderByUserService;

    @GetMapping(MESSAGE_VIEW_URL)
    public String messageView(Model model,
                              @PathVariable UUID messageId){
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if(optionalMessage.isEmpty()){
            return "redirect:"+HOME_URL;
        }
        Message message = optionalMessage.get();
        String listTo = String.join(", ",message.getTo());

        model.addAttribute("message", message);
        model.addAttribute("listTo", listTo);

        return MESSAGE_VIEW.getName();
    }

    @ModelAttribute
    void loadFoldersTemplate(Model model,@AuthenticationPrincipal OAuth2User oauthPrincipal, Principal principal){
        String username = userService.getUserId(oauthPrincipal,principal);
        LoadDefaultModel.loadTemplateWithEmails(username,model,folderByUserService);
    }
}
