package com.github.manojesus.messagesender.controller;

import com.github.manojesus.messagesender.model.EmailByUserFolder;
import com.github.manojesus.messagesender.service.EmailByUserFolderService;
import com.github.manojesus.messagesender.service.LoadDefaultModelService;
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

import static com.github.manojesus.messagesender.util.constants.AttributesNames.EMAIL_LIST;
import static com.github.manojesus.messagesender.util.constants.DefaultLabelNames.INBOX;
import static com.github.manojesus.messagesender.util.constants.UrlNames.HOME_URL;
import static com.github.manojesus.messagesender.util.constants.ViewNames.HOME;


@Controller
@AllArgsConstructor
@RequestMapping({HOME_URL})
public class AppController {


    private final EmailByUserFolderService emailByUserFolderService;
    private final UserService userService;
    private final LoadDefaultModelService loadDefaultModel;

    @GetMapping()
    public String homePage(@AuthenticationPrincipal OAuth2User oauthPrincipal, Principal principal, Model model){
        loadEmailsByUserAndLabel(INBOX,oauthPrincipal,principal,model);
        return HOME.getName();
    }

    @GetMapping("/{labelName}")
    public String emailsInLabel(@PathVariable String labelName,@AuthenticationPrincipal OAuth2User oauthPrincipal, Principal principal, Model model){
        if(labelName.equals(INBOX)){
            return "redirect:"+HOME_URL;
        }
        loadEmailsByUserAndLabel(labelName,oauthPrincipal,principal,model);
        return HOME.getName();
    }

    @ModelAttribute
    void loadFoldersTemplate(Model model,@AuthenticationPrincipal OAuth2User oauthPrincipal, Principal principal){
        String username = userService.getUserId(oauthPrincipal,principal);
        loadDefaultModel.loadTemplateWithEmails(username,model);
    }

    private void loadEmailsByUserAndLabel(String labelName, OAuth2User oauthPrincipal, Principal principal, Model model) {
        String userName = userService.getUserId(oauthPrincipal, principal);

        List<EmailByUserFolder> emailList = emailByUserFolderService.findAllByUserAndLabelName(userName.toLowerCase(), labelName);
        model.addAttribute(EMAIL_LIST, emailList);
    }
}
