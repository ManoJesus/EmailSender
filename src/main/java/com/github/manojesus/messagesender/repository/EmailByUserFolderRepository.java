package com.github.manojesus.messagesender.repository;

import com.github.manojesus.messagesender.model.EmailByUserFolder;
import com.github.manojesus.messagesender.model.primarykey.EmailByUserFolderPrimaryKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailByUserFolderRepository extends CassandraRepository<EmailByUserFolder, EmailByUserFolderPrimaryKey> {
    List<EmailByUserFolder> findAllByKey_UserIdAndKey_LabelName(String userId, String labelName);
}
