package com.github.manojesus.messagesender.model.primarykey;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.UUID;

import static org.springframework.data.cassandra.core.cql.Ordering.DESCENDING;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.CLUSTERED;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;
import static org.springframework.data.cassandra.core.mapping.CassandraType.Name.TEXT;
import static org.springframework.data.cassandra.core.mapping.CassandraType.Name.TIMEUUID;

@Data
@AllArgsConstructor
@PrimaryKeyClass
public class EmailByUserFolderPrimaryKey {
    @PrimaryKeyColumn(name = "user_id", ordinal = 0,type = PARTITIONED)
    @CassandraType(type = TEXT)
    private String userId;
    @PrimaryKeyColumn(name = "label_name", ordinal = 1,type = PARTITIONED)
    @CassandraType(type = TEXT)
    private String labelName;
    @PrimaryKeyColumn(name = "message_id", ordinal = 2,type = CLUSTERED, ordering = DESCENDING)
    @CassandraType(type = TIMEUUID)
    private UUID messageId;
}
