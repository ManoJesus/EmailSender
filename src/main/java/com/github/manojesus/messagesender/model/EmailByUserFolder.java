package com.github.manojesus.messagesender.model;

import com.github.manojesus.messagesender.model.primarykey.EmailByUserFolderPrimaryKey;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;

import static org.springframework.data.cassandra.core.mapping.CassandraType.Name.BOOLEAN;
import static org.springframework.data.cassandra.core.mapping.CassandraType.Name.TEXT;

@Table("email_by_user_folder")
@Builder
@Data
public class EmailByUserFolder {
    @PrimaryKey
    private EmailByUserFolderPrimaryKey key;
    @CassandraType(type = TEXT, typeArguments = TEXT)
    private List<String> to;
    @CassandraType(type = TEXT)
    private String subject;
    @CassandraType(type = BOOLEAN)
    private boolean isRead;
}
