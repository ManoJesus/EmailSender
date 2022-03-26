package com.github.manojesus.messagesender.service;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.github.manojesus.messagesender.model.Message;
import com.github.manojesus.messagesender.model.MessageForm;
import com.github.manojesus.messagesender.repository.MessageRepository;
import com.github.manojesus.messagesender.repository.UnreadEmailStatsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.manojesus.messagesender.util.DefaultLabelNames.INBOX;
import static com.github.manojesus.messagesender.util.DefaultLabelNames.SENT;

@Service
@AllArgsConstructor
public class MessageService {


    private final EmailByUserFolderService emailByUserFolderService;
    private final MessageRepository messageRepository;
    private final UnreadEmailStatsRepository unreadEmailStatusRepository;


    public void sentEmail(MessageForm messageForm, String username){
        Message messageToBeSaved = Message.builder()
                .messageId(Uuids.timeBased())
                .from(username)
                .to(Stream.of(messageForm.getTo().split(","))
                        .map(StringUtils::trimWhitespace)
                        .filter(StringUtils::hasText)
                        .distinct()
                        .collect(Collectors.toList()))
                .body(messageForm.getBody())
                .subject(messageForm.getSubject())
                .build();
        messageRepository.save(messageToBeSaved);

        //Save email in the user's sent folder
        emailByUserFolderService.saveMessageInFolderList(SENT,messageToBeSaved, username);
        unreadEmailStatusRepository.incrementCounter(username,SENT);

        //Save email in the users' inbox folder, for all "to" users
        for(String ToUsername : messageToBeSaved.getTo()){
            emailByUserFolderService.saveMessageInFolderList(INBOX,messageToBeSaved, ToUsername);
            unreadEmailStatusRepository.incrementCounter(username,INBOX);
        }
    }
}
