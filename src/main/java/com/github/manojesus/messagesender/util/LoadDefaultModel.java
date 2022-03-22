package com.github.manojesus.messagesender.util;

import com.github.manojesus.messagesender.model.FolderByUser;
import com.github.manojesus.messagesender.repository.MessageRepository;
import com.github.manojesus.messagesender.service.FolderByUserService;
import com.github.manojesus.messagesender.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;
@Component
@AllArgsConstructor
public final class LoadDefaultModel {

    public static void loadTemplateWithEmails(String username,
                                              Model model,
                                              FolderByUserService folderByUserService ) {

        List<FolderByUser> defaultFolders = folderByUserService.createDefaultFolders(username);
        List<FolderByUser> userFolders = folderByUserService.findAllFolderCreatedByUsers(username);

        model.addAttribute("userName", username);
        model.addAttribute("defaultFolders", defaultFolders);
        model.addAttribute("folders", userFolders);

    }
}
