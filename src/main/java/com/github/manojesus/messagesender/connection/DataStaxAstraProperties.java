package com.github.manojesus.messagesender.connection;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.File;

@ConfigurationProperties(prefix = "datastax.astra")
@Data
public class DataStaxAstraProperties {
    private File secureConnectBundle;

    public void setSecureConnectBundle(File secureConnectBundle) {
        this.secureConnectBundle = secureConnectBundle;
    }
}
