package com.github.manojesus.messagesender.util;

import com.github.manojesus.messagesender.model.FolderByUser;
import com.github.manojesus.messagesender.service.FolderByUserService;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;

import static com.github.manojesus.messagesender.util.AttributesNames.*;
import static com.github.manojesus.messagesender.util.AttributesNames.DEFAULT_FOLDERS_LIST;

@Component
public final class LoadDefaultModel {

    public static void loadTemplateWithEmails(String username,
                                              Model model,
                                              FolderByUserService folderByUserService ) {

        List<FolderByUser> defaultFolders = folderByUserService.findAllDefaultFolder(username);
        List<FolderByUser> userFolders = folderByUserService.findAllFolderCreatedByUsers(username);

        model.addAttribute(DEFAULT_FOLDERS_LIST, defaultFolders);
        model.addAttribute(USER_FOLDERS_LIST, userFolders);
    }
}
