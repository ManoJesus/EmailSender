package com.github.manojesus.messagesender.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Builder
@Table("unread_emails_stats")
public class UnreadEmailStats {
    @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String userId;
    @PrimaryKeyColumn(name = "label_name", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private String labelName;
    @CassandraType(type = CassandraType.Name.COUNTER)
    private Integer unread;
}
