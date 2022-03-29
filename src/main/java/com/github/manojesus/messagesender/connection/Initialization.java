package com.github.manojesus.messagesender.connection;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.github.manojesus.messagesender.model.*;
import com.github.manojesus.messagesender.model.primarykey.EmailByUserFolderPrimaryKey;
import com.github.manojesus.messagesender.repository.*;
import com.github.manojesus.messagesender.service.FolderByUserService;
import com.github.manojesus.messagesender.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;

import java.util.List;
import java.util.UUID;

import static com.github.manojesus.messagesender.enums.FolderType.USER_CREATED;
import static com.github.manojesus.messagesender.util.constants.DefaultLabelNames.*;

@Configuration
@AllArgsConstructor
public class Initialization {

    private static final String DEFAULT_USER = "lucas@email.com";

    private final FolderByUserRepository folderByUserRepository;
    private final EmailByUserFolderRepository emailByUserFolderRepository;
    private final UserRepository userRepository;
    private final MessageService messageService;
    private final BCryptPasswordEncoder encoder;
    private final UnreadEmailStatsRepository unreadEmailStatsRepository;
    private FolderByUserService folderByUserService;

    @PostConstruct
    public void init(){
        //Creating some user
        createUser();
        // Creating folder for a default user
        createFolders();

        //Creating emails for a default user and default folder
        createEmails(INBOX);
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

        folderByUserService.createDefaultFolders(DEFAULT_USER);

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
            String subject = "subject "+i;

            MessageForm messageToBeSaved = MessageForm.builder()
                    .to("lucas@email.com, albert@email.com")
                    .subject(subject)
                    .body("body: "+i*5)
                    .build();
            messageService.sentEmail(messageToBeSaved,DEFAULT_USER);
        }
    }


}
