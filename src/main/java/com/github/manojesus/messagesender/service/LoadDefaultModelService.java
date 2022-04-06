package com.github.manojesus.messagesender.service;

import com.github.manojesus.messagesender.model.FolderByUser;
import com.github.manojesus.messagesender.model.UnreadEmailStats;
import com.github.manojesus.messagesender.repository.UnreadEmailStatsRepository;
import com.github.manojesus.messagesender.util.constants.AttributesNames;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.manojesus.messagesender.util.constants.AttributesNames.DEFAULT_FOLDERS_LIST;
import static com.github.manojesus.messagesender.util.constants.AttributesNames.USER_FOLDERS_LIST;
@Service
@AllArgsConstructor
public class LoadDefaultModelService {

    private final FolderByUserService folderByUserService;
    private UnreadEmailStatsRepository unreadEmailStatsRepository;

    public void loadTemplateWithEmails(String username,
                                              Model model) {

        List<FolderByUser> defaultFolders = folderByUserService.findAllDefaultFolder(username);
        List<FolderByUser> userFolders = folderByUserService.findAllFolderCreatedByUsers(username);

        Map<String, Integer> statsRead = unreadEmailStatsRepository.findAllByUserId(username)
                .stream()
                .collect(Collectors.toMap(UnreadEmailStats::getLabelName, UnreadEmailStats::getUnread));

        model.addAttribute(AttributesNames.EMAIL_STATS_LIST, statsRead);
        model.addAttribute(DEFAULT_FOLDERS_LIST, defaultFolders);
        model.addAttribute(USER_FOLDERS_LIST, userFolders);
    }
}
