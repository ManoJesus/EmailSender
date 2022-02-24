package com.github.manojesus.messagesender.connection;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.github.manojesus.messagesender.model.EmailByUserFolder;
import com.github.manojesus.messagesender.model.FolderByUser;
import com.github.manojesus.messagesender.model.Message;
import com.github.manojesus.messagesender.model.User;
import com.github.manojesus.messagesender.model.primarykey.EmailByUserFolderPrimaryKey;
import com.github.manojesus.messagesender.repository.EmailByUserFolderRepository;
import com.github.manojesus.messagesender.repository.FolderByUserRepository;
import com.github.manojesus.messagesender.repository.MessageRepository;
import com.github.manojesus.messagesender.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;

import java.util.List;
import java.util.UUID;

import static com.github.manojesus.messagesender.enums.FolderType.USER_CREATED;

@Configuration
@AllArgsConstructor
public class Initialization {

    private static final String DEFAULT_USER = "manojesus";

    private final FolderByUserRepository folderByUserRepository;
    private final EmailByUserFolderRepository emailByUserFolderRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final BCryptPasswordEncoder encoder;

    @PostConstruct
    public void init(){
        //Creating some user
        createUser();
        // Creating folder for a default user
        createFolders();

        //Creating emails for a default user and default folder
        createEmails("inbox");
        createEmails("sent");
    }

    private void createFolders() {
        folderByUserRepository.saveAll(List.of(
                FolderByUser.builder()
                        .userId(DEFAULT_USER)
                        .labelName("important")
                        .labelColor("green").folderType(USER_CREATED).build(),
                FolderByUser.builder()
                        .userId(DEFAULT_USER)
                        .labelName("from work")
                        .labelColor("blue").folderType(USER_CREATED).build(),
                FolderByUser.builder()
                        .userId(DEFAULT_USER)
                        .labelName("ignore")
                        .labelColor("red").folderType(USER_CREATED).build()));
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
            emailToSaveInList.setRead(false);
            emailToSaveInList.setTo(toList);
            emailToSaveInList.setSubject(subject);
            emailByUserFolderRepository.save(emailToSaveInList);
        }
    }


}
