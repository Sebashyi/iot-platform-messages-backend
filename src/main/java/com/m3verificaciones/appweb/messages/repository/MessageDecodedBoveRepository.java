package com.m3verificaciones.appweb.messages.repository;

import com.m3verificaciones.appweb.messages.model.MessageDecodedBove;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface MessageDecodedBoveRepository extends CrudRepository<MessageDecodedBove, String> {
    @Query("SELECT m FROM MessageDecodedBove m ORDER BY m.createdAt DESC")
    List<MessageDecodedBove> findAllMessagesOrderedBySendAt();

    @Query("SELECT m FROM MessageDecodedBove m WHERE m.devEui = :devEui ORDER BY m.createdAt DESC")
    List<MessageDecodedBove> findAllByDevEuiOrderBySendMessageTimeDesc(String devEui);
}
