package com.github.manojesus.messagesender.repository;

import com.github.manojesus.messagesender.model.FolderByUser;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserUnreadEmailStats extends CassandraRepository<UserUnreadEmailStats, String> {

}
