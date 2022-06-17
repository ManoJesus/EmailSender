package com.github.manojesus.messagesender.service;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.github.manojesus.messagesender.model.Message;
import com.github.manojesus.messagesender.model.MessageForm;
import com.github.manojesus.messagesender.repository.MessageRepository;
import com.github.manojesus.messagesender.repository.UnreadEmailStatsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.manojesus.messagesender.util.constants.DefaultLabelNames.INBOX;
import static com.github.manojesus.messagesender.util.constants.DefaultLabelNames.SENT;

@Service
@AllArgsConstructor
@Slf4j
public class MessageService {


    private final EmailByUserFolderService emailByUserFolderService;
    private final MessageRepository messageRepository;
    private final UnreadEmailStatsRepository unreadEmailStatusRepository;


    public void sentEmail(MessageForm messageForm, String username){
        log.info("Saving the follow message {}", messageForm.toString());
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

        //Save email in the users' inbox folder, for all "to" users
        for(String toUsername : messageToBeSaved.getTo()){
            log.info("Increment the unread counter from {}",toUsername);
            emailByUserFolderService.saveMessageInFolderList(INBOX,messageToBeSaved, toUsername);
            unreadEmailStatusRepository.incrementCounter(toUsername,INBOX);
        }
    }
}
