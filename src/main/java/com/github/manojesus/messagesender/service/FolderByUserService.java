package com.github.manojesus.messagesender.service;

import com.github.manojesus.messagesender.model.FolderByUser;
import com.github.manojesus.messagesender.model.User;
import com.github.manojesus.messagesender.repository.FolderByUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.github.manojesus.messagesender.enums.FolderType.DEFAULT_FOLDER;
import static com.github.manojesus.messagesender.enums.FolderType.USER_CREATED;
import static com.github.manojesus.messagesender.util.DefaultLabelNames.*;

@Service
@AllArgsConstructor
public class FolderByUserService {

    private final FolderByUserRepository folderByUserRepository;
    private final UserService userService;

    public void createDefaultFolders(final String userId){
        folderByUserRepository.saveAll(List.of(
                FolderByUser.builder().userId(userId.toLowerCase()).labelName(INBOX).labelColor("white").folderType(DEFAULT_FOLDER).build(),
                FolderByUser.builder().userId(userId.toLowerCase()).labelName( SENT).labelColor("white").folderType(DEFAULT_FOLDER).build(),
                FolderByUser.builder().userId(userId.toLowerCase()).labelName( JUNK).labelColor("white").folderType(DEFAULT_FOLDER).build()
        ));
    }



    public List<FolderByUser> findAllFolderCreatedByUsers(final String username) {
        return folderByUserRepository.findAllByUserId(username.toLowerCase()).stream()
                .filter(user -> user.getFolderType().name().equals(USER_CREATED.name()))
                .collect(Collectors.toList());
    }

    public List<FolderByUser> findAllDefaultFolder(String username) {
        return folderByUserRepository.findAllByUserId(username.toLowerCase()).stream()
                .filter(user -> user.getFolderType().name().equals(DEFAULT_FOLDER.name()))
                .collect(Collectors.toList());
    }
}
