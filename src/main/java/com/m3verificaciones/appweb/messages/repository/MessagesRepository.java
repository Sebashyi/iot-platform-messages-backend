package com.m3verificaciones.appweb.messages.repository;

import com.m3verificaciones.appweb.messages.model.Messages;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface MessagesRepository extends CrudRepository<Messages, String> {
    @Query("SELECT m FROM Messages m ORDER BY m.createdAt DESC")
    List<Messages> findAllMessagesOrderedByCreatedAt();

    List<Messages> findBySerialMeterIn(List<String> serials);

    // Método a añadir en MessagesRepository.java
    List<Messages> findBySerialMeterInOrderByCreatedAtDesc(List<String> serials);
}
