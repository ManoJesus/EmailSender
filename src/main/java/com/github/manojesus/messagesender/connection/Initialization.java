package com.github.manojesus.messagesender.connection;

import com.github.manojesus.messagesender.model.FolderByUser;
import com.github.manojesus.messagesender.repository.FolderByUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import java.util.List;

import static com.github.manojesus.messagesender.enums.FolderType.USER_CREATED;

@Configuration
@AllArgsConstructor
public class Initialization {

    private final FolderByUserRepository folderByUserRepository;


    @PostConstruct
    public void init(){
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
            }


}
