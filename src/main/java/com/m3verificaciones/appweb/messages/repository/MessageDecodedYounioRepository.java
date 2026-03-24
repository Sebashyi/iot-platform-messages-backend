package com.m3verificaciones.appweb.messages.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.m3verificaciones.appweb.messages.model.MessageDecodedYounio;

public interface MessageDecodedYounioRepository extends CrudRepository<MessageDecodedYounio, String> {
    @Query("SELECT m FROM MessageDecodedYounio m ORDER BY m.createdAt DESC")
    List<MessageDecodedYounio> findAllMessagesOrderedBySendAt();

    @Query("SELECT m FROM MessageDecodedYounio m WHERE m.devEui = :devEui ORDER BY m.createdAt DESC")
    List<MessageDecodedYounio> findAllByDevEuiOrderBySendMessageTimeDesc(String devEui);

    @Query("""
            SELECT m FROM MessageDecodedYounio m
            WHERE m.devEui IN :devEuis
              AND (m.lowBatteryAlarm = true OR m.emptyPipeAlarm = true OR m.reverseFlowAlarm = true
                OR m.overRangeAlarm = true OR m.overTemperatureAlarm = true OR m.eepromError = true
                OR m.leakageAlarm = true OR m.burstAlarm = true OR m.tamperAlarm = true
                OR m.freezingAlarm = true)
            ORDER BY m.createdAt DESC
            """)
    List<MessageDecodedYounio> findAlertsbyDevEuis(@Param("devEuis") List<String> devEuis);
}
