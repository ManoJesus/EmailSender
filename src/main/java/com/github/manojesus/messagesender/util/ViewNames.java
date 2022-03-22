package com.github.manojesus.messagesender.util;

public enum ViewNames {
    HOME("home"), INDEX("index"),MESSAGE_VIEW("message-view"),SIGNUP("signup"),COMPOSE_MESSAGE("compose-message");

    private final String name;

    ViewNames(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
