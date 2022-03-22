package com.github.manojesus.messagesender.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class MessageForm {
    private String to;
    private String subject;
    private String body;
}
