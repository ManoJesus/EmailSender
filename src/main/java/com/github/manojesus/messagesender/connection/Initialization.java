package com.github.manojesus.messagesender.connection;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.github.manojesus.messagesender.model.EmailByUserFolder;
import com.github.manojesus.messagesender.model.FolderByUser;
import com.github.manojesus.messagesender.model.Message;
import com.github.manojesus.messagesender.model.User;
import com.github.manojesus.messagesender.model.primarykey.EmailByUserFolderPrimaryKey;
import com.github.manojesus.messagesender.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;

import java.util.List;
import java.util.UUID;

import static com.github.manojesus.messagesender.enums.FolderType.DEFAULT_FOLDER;
import static com.github.manojesus.messagesender.enums.FolderType.USER_CREATED;
import static com.github.manojesus.messagesender.util.DefaultLabelNames.*;

@Configuration
@AllArgsConstructor
public class Initialization {

    private static final String DEFAULT_USER = "manojesus";

    private final FolderByUserRepository folderByUserRepository;
    private final EmailByUserFolderRepository emailByUserFolderRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final BCryptPasswordEncoder encoder;
    private final UnreadEmailStatsRepository unreadEmailStatsRepository;

    @PostConstruct
    public void init(){
        //Creating some user
        createUser();
        // Creating folder for a default user
        createFolders();

        //Creating emails for a default user and default folder
        createEmails(INBOX);
        createEmails(SENT);
    }

    private void createFolders() {
        folderByUserRepository.saveAll(List.of(
                FolderByUser.builder()
                        .userId(DEFAULT_USER)
                        .labelName(IMPORTANT)
                        .labelColor("green").folderType(USER_CREATED).build(),
                FolderByUser.builder()
                        .userId(DEFAULT_USER)
                        .labelName("From work")
                        .labelColor("blue").folderType(USER_CREATED).build(),
                FolderByUser.builder()
                        .userId(DEFAULT_USER)
                        .labelName("Ignore")
                        .labelColor("red").folderType(USER_CREATED).build()));

        unreadEmailStatsRepository.incrementCounter(DEFAULT_USER,INBOX);
        unreadEmailStatsRepository.incrementCounter(DEFAULT_USER,INBOX);
        unreadEmailStatsRepository.incrementCounter(DEFAULT_USER,INBOX);
        unreadEmailStatsRepository.incrementCounter(DEFAULT_USER,INBOX);
    }

    private void createUser() {
        userRepository.save(User.builder()
                .email("lucas@email.com")
                .firstName("Lucas")
                .lastName("Pinheiro")
                .password(encoder.encode("12345")).build());
    }

    private void createEmails(String labelName) {
        for(int i = 0; i < 10; i++){
            UUID messageUuid = Uuids.timeBased();
            List<String> toList = List.of(DEFAULT_USER);
            String subject = "subject "+i;

            Message messageToBeSaved = Message.builder()
                    .messageId(messageUuid)
                    .to(toList)
                    .subject("subject "+i)
                    .from(DEFAULT_USER)
                    .body("body: "+i*5)
                    .build();
            messageRepository.save(messageToBeSaved);

            EmailByUserFolder emailToSaveInList = new EmailByUserFolder();

            emailToSaveInList.setKey(new EmailByUserFolderPrimaryKey(DEFAULT_USER, labelName, messageUuid));
            emailToSaveInList.setEmailSentTime("");
            if(labelName.equals(SENT)){
                emailToSaveInList.setRead(true);
            }
            emailToSaveInList.setRead(false);
            emailToSaveInList.setTo(toList);
            emailToSaveInList.setSubject(subject);
            emailByUserFolderRepository.save(emailToSaveInList);
        }
    }


}
