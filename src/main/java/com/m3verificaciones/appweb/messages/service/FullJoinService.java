package com.m3verificaciones.appweb.messages.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.m3verificaciones.appweb.messages.model.MessageDecodedBove;
import com.m3verificaciones.appweb.messages.model.MessageDecodedYounio;
import com.m3verificaciones.appweb.messages.repository.MessageDecodedBoveRepository;
import com.m3verificaciones.appweb.messages.repository.MessageDecodedYounioRepository;
import com.m3verificaciones.appweb.messages.repository.MeterRepository;
import com.m3verificaciones.appweb.messages.model.Meter;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FullJoinService {
    private final MessageDecodedBoveRepository messageDecodedBoveRepository;
    private final MessageDecodedYounioRepository messageDecodedYounioRepository;
    private final MeterRepository meterRepository;

    public FullJoinService(MessageDecodedBoveRepository messageDecodedBoveRepository,
            MessageDecodedYounioRepository messageDecodedYounioRepository,
            MeterRepository meterRepository) {
        this.messageDecodedBoveRepository = messageDecodedBoveRepository;
        this.messageDecodedYounioRepository = messageDecodedYounioRepository;
        this.meterRepository = meterRepository;
    }

    public List<Object> getLatestRecords() {
        List<MessageDecodedYounio> tableARecords = messageDecodedYounioRepository.findAllMessagesOrderedBySendAt();
        List<MessageDecodedBove> tableBRecords = messageDecodedBoveRepository.findAllMessagesOrderedBySendAt();

        List<Object> result = new ArrayList<>();
        result.addAll(tableARecords);
        result.addAll(tableBRecords);

        return result;
    }

    public List<Object> getAlertMessagesByCompany(String companyUniqueKey) {
        List<String> devEuis = meterRepository.findByCompanyUniqueKey(companyUniqueKey)
                .stream()
                .map(Meter::getDevEui)
                .filter(devEui -> devEui != null && !devEui.isBlank())
                .distinct()
                .toList();

        if (devEuis.isEmpty()) {
            return List.of();
        }

        List<Object> result = new ArrayList<>();
        result.addAll(messageDecodedBoveRepository.findAlertsbyDevEuis(devEuis));
        result.addAll(messageDecodedYounioRepository.findAlertsbyDevEuis(devEuis));

        result.sort((a, b) -> {
            LocalDateTime timeA = a instanceof MessageDecodedBove bove
                    ? bove.getCreatedAt()
                    : ((MessageDecodedYounio) a).getCreatedAt();
            LocalDateTime timeB = b instanceof MessageDecodedBove bove
                    ? bove.getCreatedAt()
                    : ((MessageDecodedYounio) b).getCreatedAt();
            return timeB.compareTo(timeA); // DESC
        });

        return result;
    }
}
