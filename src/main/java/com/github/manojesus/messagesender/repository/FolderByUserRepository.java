package com.github.manojesus.messagesender.repository;

import com.github.manojesus.messagesender.model.FolderByUser;
import com.github.manojesus.messagesender.enums.FolderType;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderByUserRepository extends CassandraRepository<FolderByUser, String> {
    List<FolderByUser> findAllByUserId(String id);
}
