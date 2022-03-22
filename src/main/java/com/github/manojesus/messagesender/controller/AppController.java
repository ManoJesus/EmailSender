package com.github.manojesus.messagesender.controller;

import com.github.manojesus.messagesender.controller.util.LoadDefaultModel;
import com.github.manojesus.messagesender.model.EmailByUserFolder;
import com.github.manojesus.messagesender.model.FolderByUser;
import com.github.manojesus.messagesender.service.EmailByUserFolderService;
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
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;


@Controller
@AllArgsConstructor
@RequestMapping("/home")
public class AppController {

    private static final String DEFAULT_FOLDER = "inbox";

    private final FolderByUserService folderByUserService;
    private final EmailByUserFolderService emailByUserFolderService;
    private final UserService userService;

    @GetMapping()
    public String homePage(@AuthenticationPrincipal OAuth2User oauthPrincipal, Principal principal, Model model){
        loadEmailsByUserAndLabel(DEFAULT_FOLDER,oauthPrincipal,principal,model);
        return "inbox";
    }

    @GetMapping("/{labelName}")
    public String emailsInLabel(@PathVariable String labelName,@AuthenticationPrincipal OAuth2User oauthPrincipal, Principal principal, Model model){
        if(labelName.equals("inbox")){
            return "redirect:/home";
        }
        loadEmailsByUserAndLabel(labelName,oauthPrincipal,principal,model);
        return "inbox";
    }
    @ModelAttribute
    void loadFoldersTemplate(Model model,@AuthenticationPrincipal OAuth2User oauthPrincipal, Principal principal){
        String username = userService.getUserId(oauthPrincipal,principal);
        LoadDefaultModel.loadTemplateWithEmails(username,model,folderByUserService);
    }
    private void loadEmailsByUserAndLabel(String labelName, OAuth2User oauthPrincipal, Principal principal, Model model) {
        String userName = userService.getUserId(oauthPrincipal, principal);

//        List<FolderByUser> defaultFolders = folderByUserService.createDefaultFolders(userName);
//        List<FolderByUser> userFolders = folderByUserService.findAllFolderCreatedByUsers(userName);
//
//        model.addAttribute("userName", userName);
//        model.addAttribute("defaultFolders", defaultFolders);
//        model.addAttribute("folders", userFolders);
        List<EmailByUserFolder> emailList = emailByUserFolderService.findAllByUserAndLabelName(userName.toLowerCase(), labelName);
        model.addAttribute("emailList", emailList);
    }
}
