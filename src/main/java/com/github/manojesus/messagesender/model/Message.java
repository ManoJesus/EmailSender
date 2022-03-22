package com.github.manojesus.messagesender.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.swing.text.DateFormatter;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;
import static org.springframework.data.cassandra.core.mapping.CassandraType.Name.DATE;
import static org.springframework.data.cassandra.core.mapping.CassandraType.Name.LIST;
import static org.springframework.data.cassandra.core.mapping.CassandraType.Name.TEXT;
import static org.springframework.data.cassandra.core.mapping.CassandraType.Name.TIMEUUID;

@Data
@Builder
@Table("massages_by_id")
public class Message {
    @Id // when just one primary key, must add this id annotation
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
    @CassandraType(type = DATE)
    @Builder.Default
    private LocalDate sentDate = LocalDate.now();
}
