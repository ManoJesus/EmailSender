package com.github.manojesus.messagesender.controller;

import com.github.manojesus.messagesender.model.FolderByUser;
import com.github.manojesus.messagesender.service.FolderByUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;


@Controller
@AllArgsConstructor
public class FoldersController {

    private static final String DEFAULT_FOLDER = "inbox";

    private final FolderByUserService folderByUserService;

    @GetMapping("/inbox")
    public String homePage(@AuthenticationPrincipal OAuth2User oauthPrincipal, Principal principal, Model model){
        String userName = folderByUserService.getUserId(oauthPrincipal, principal);

        //---------- Fetching all folder by username
        List<FolderByUser> defaultFolders = folderByUserService.createDefaultFolders(userName);
        List<FolderByUser> userFolders = folderByUserService.findAllFolderCreatedByUsers(userName);

        model.addAttribute("userName", userName);
        model.addAttribute("defaultFolders", defaultFolders);
        model.addAttribute("folders", userFolders);

        //---------- Fetching all emails from a folder


        return "inbox";
    }
}
