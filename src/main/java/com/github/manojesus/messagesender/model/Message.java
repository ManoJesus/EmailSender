package com.github.manojesus.messagesender.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@Table("massages_by_id")
public class Message {
    @PrimaryKeyColumn(name = "message_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    @CassandraType(type = CassandraType.Name.TIMEUUID)
    private LocalDate messageId;
    private String from;
    private List<String> to;
    private String subject;
    private String body;
}
