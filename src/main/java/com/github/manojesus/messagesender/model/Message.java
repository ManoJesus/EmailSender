package com.github.manojesus.messagesender.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.*;
import static org.springframework.data.cassandra.core.mapping.CassandraType.Name.*;

@Data
@Builder
@Table("massages_by_id")
public class Message {
    @PrimaryKeyColumn(name = "message_id", ordinal = 0, type = PARTITIONED)
    @CassandraType(type = TIMEUUID)
    private UUID messageId;
    @CassandraType(type = TEXT)
    private String from;
    @CassandraType(type = LIST, typeArguments = TEXT)
    private List<String> to;
    @CassandraType(type = TEXT)
    private String subject;
    @CassandraType(type = TEXT)
    private String body;
}
