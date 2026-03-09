package com.m3verificaciones.appweb.messages.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.m3verificaciones.appweb.messages.model.MessageDecodedYounio;

public interface MessageDecodedYounioRepository extends CrudRepository<MessageDecodedYounio, String>{
    @Query("SELECT m FROM MessageDecodedYounio m ORDER BY m.createdAt DESC")
    List<MessageDecodedYounio> findAllMessagesOrderedBySendAt();

    @Query("SELECT m FROM MessageDecodedYounio m WHERE m.devEui = :devEui ORDER BY m.createdAt DESC")
    List<MessageDecodedYounio> findAllByDevEuiOrderBySendMessageTimeDesc(String devEui);
}
