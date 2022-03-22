package com.github.manojesus.messagesender.service;

import com.github.manojesus.messagesender.model.Message;
import com.github.manojesus.messagesender.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageService {

    private static final String DEFAULT_LABEL_TO_SAVE = "sent";

    private EmailByUserFolderService emailByUserFolderService;
    private MessageRepository messageRepository;

    public void sentEmail(Message messageToSent){
        messageRepository.save(messageToSent);
        emailByUserFolderService.saveMessageInFolderList(DEFAULT_LABEL_TO_SAVE,messageToSent);
    }
}
