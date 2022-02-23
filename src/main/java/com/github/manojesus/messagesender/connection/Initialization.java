package com.github.manojesus.messagesender.connection;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.github.manojesus.messagesender.model.EmailByUserFolder;
import com.github.manojesus.messagesender.model.FolderByUser;
import com.github.manojesus.messagesender.model.User;
import com.github.manojesus.messagesender.model.primarykey.EmailByUserFolderPrimaryKey;
import com.github.manojesus.messagesender.repository.EmailByUserFolderRepository;
import com.github.manojesus.messagesender.repository.FolderByUserRepository;
import com.github.manojesus.messagesender.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.github.manojesus.messagesender.enums.FolderType.USER_CREATED;

@Configuration
@AllArgsConstructor
public class Initialization {

    private final FolderByUserRepository folderByUserRepository;
    private final EmailByUserFolderRepository emailByUserFolderRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @PostConstruct
    public void init(){
        //Creating default user
        userRepository.save(User.builder()
                .email("lucas@email.com")
                .firstName("Lucas")
                .lastName("Pinheiro")
                .password(encoder.encode("12345")).build());
        // Creating folder for a default user
        folderByUserRepository.saveAll(List.of(
                FolderByUser.builder()
                        .userId("manojesus")
                        .labelName("important")
                        .labelColor("green").folderType(USER_CREATED).build(),
                FolderByUser.builder()
                        .userId("manojesus")
                        .labelName("from work")
                        .labelColor("blue").folderType(USER_CREATED).build(),
                FolderByUser.builder()
                        .userId("manojesus")
                        .labelName("ignore")
                        .labelColor("red").folderType(USER_CREATED).build()));

        //Creating emails for a default user and default folder
        emailByUserFolderRepository.saveAll(
                List.of(
                        EmailByUserFolder.builder()
                                .key(new EmailByUserFolderPrimaryKey("manojesus","inbox", Uuids.timeBased()))
                                .isRead(false)
                                .to(Collections.singletonList("manojesus"))
                                .subject("Construction")
                                .build(),
                        EmailByUserFolder.builder()
                                .key(new EmailByUserFolderPrimaryKey("manojesus","inbox", Uuids.timeBased()))
                                .isRead(false)
                                .to(Collections.singletonList("manojesus"))
                                .subject("Work")
                                .build()

                )
        );
    }


}
