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

@Service
@AllArgsConstructor
public class FolderByUserService {

    private final FolderByUserRepository folderByUserRepository;
    private final UserService userService;

    public List<FolderByUser> createDefaultFolders(final String userId){
        return folderByUserRepository.saveAll(List.of(
                FolderByUser.builder().userId(userId).labelName( "inbox").labelColor("white").folderType(DEFAULT_FOLDER).build(),
                FolderByUser.builder().userId(userId).labelName( "sent").labelColor("white").folderType(DEFAULT_FOLDER).build(),
                FolderByUser.builder().userId(userId).labelName( "spam").labelColor("white").folderType(DEFAULT_FOLDER).build()
        ));
    }

    public String getUserId(OAuth2User oauthPrincipal, Principal principal) {
        String userId = "";
        if(oauthPrincipal != null && StringUtils.hasText(oauthPrincipal.getAttribute("login"))) {
            userId = Objects.requireNonNull(oauthPrincipal.getAttribute("login")).toString().toLowerCase();
        }else{
            User user = userService.findByEmail(principal.getName());
            userId = user.getFirstName();
        }
        return userId;
    }

    public List<FolderByUser> findAllFolderCreatedByUsers(String userName) {
        return folderByUserRepository.findAllByUserId(userName).stream()
                .filter(user -> user.getFolderType().name().equals(USER_CREATED.name()))
                .collect(Collectors.toList());
    }
}
