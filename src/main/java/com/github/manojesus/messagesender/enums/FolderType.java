package com.github.manojesus.messagesender.enums;

import lombok.Getter;

@Getter
public
enum FolderType {
    USER_CREATED("user_created"), DEFAULT_FOLDER("default_folder");

    private final String name;

    FolderType(String name) {
        this.name = name;
    }
}
