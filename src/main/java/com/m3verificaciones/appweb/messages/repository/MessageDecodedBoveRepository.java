package com.m3verificaciones.appweb.messages.repository;

import com.m3verificaciones.appweb.messages.model.MessageDecodedBove;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MessageDecodedBoveRepository extends CrudRepository<MessageDecodedBove, String> {
    @Query("SELECT m FROM MessageDecodedBove m ORDER BY m.createdAt DESC")
    List<MessageDecodedBove> findAllMessagesOrderedBySendAt();

    @Query("SELECT m FROM MessageDecodedBove m WHERE m.devEui = :devEui ORDER BY m.createdAt DESC")
    List<MessageDecodedBove> findAllByDevEuiOrderBySendMessageTimeDesc(String devEui);

    @Query("""
            SELECT m FROM MessageDecodedBove m
            WHERE m.devEui IN :devEuis
              AND (m.lowBatteryAlarm = true OR m.emptyPipeAlarm = true OR m.reverseFlowAlarm = true
                OR m.overRangeAlarm = true OR m.overTempratureAlarm = true OR m.eepromError = true
                OR m.leakageAlarm = true OR m.burstAlarm = true OR m.tamperAlarm = true
                OR m.freezingAlarm = true)
            ORDER BY m.createdAt DESC
            """)
    List<MessageDecodedBove> findAlertsbyDevEuis(@Param("devEuis") List<String> devEuis);
}
