package com.github.manojesus.messagesender.service;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.github.manojesus.messagesender.model.Message;
import com.github.manojesus.messagesender.model.MessageForm;
import com.github.manojesus.messagesender.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class MessageService {

    private static final String DEFAULT_FROM_LABEL_TO_SAVE = "sent";
    private static final String DEFAULT_TO_LABEL_TO_SAVE = "inbox";

    private EmailByUserFolderService emailByUserFolderService;
    private MessageRepository messageRepository;


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
                .subject(messageForm.getSubject()).build();

        messageRepository.save(messageToBeSaved);
        //Save email in the user's sent folder
        emailByUserFolderService.saveMessageInFolderList(DEFAULT_FROM_LABEL_TO_SAVE,messageToBeSaved, username);

        //Save email in the users' inbox folder, of "to" users
        for(String ToUsername : messageToBeSaved.getTo()){
            emailByUserFolderService.saveMessageInFolderList(DEFAULT_TO_LABEL_TO_SAVE,messageToBeSaved, ToUsername);
        }
    }
}
