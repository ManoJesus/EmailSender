package com.github.manojesus.messagesender.model;

import com.github.manojesus.messagesender.enums.FolderType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.CLUSTERED;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;
import static org.springframework.data.cassandra.core.mapping.CassandraType.Name.TEXT;

@Table("folders_by_user")
@Builder
@Data
public class FolderByUser {
    @CassandraType(type = TEXT)
    @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PARTITIONED)
    private String userId;
    @PrimaryKeyColumn(name = "label_name", ordinal = 1, type = CLUSTERED)
    private String labelName;
    @CassandraType(type = TEXT)
    private String labelColor;
    @CassandraType(type = TEXT)
    private FolderType folderType;
}
