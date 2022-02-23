package com.github.manojesus.messagesender.repository;

import com.github.manojesus.messagesender.model.Message;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageRepository extends CassandraRepository<Message, UUID> {
}
