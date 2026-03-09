package com.m3verificaciones.appweb.messages.service;

import java.util.ArrayList;
import java.util.List;

import com.m3verificaciones.appweb.messages.model.MessageDecodedBove;
import com.m3verificaciones.appweb.messages.model.MessageDecodedYounio;
import com.m3verificaciones.appweb.messages.repository.MessageDecodedBoveRepository;
import com.m3verificaciones.appweb.messages.repository.MessageDecodedYounioRepository;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FullJoinService {
    private final MessageDecodedBoveRepository messageDecodedBoveRepository;
    private final MessageDecodedYounioRepository messageDecodedYounioRepository;

    public FullJoinService(MessageDecodedBoveRepository messageDecodedBoveRepository, MessageDecodedYounioRepository messageDecodedYounioRepository) {
        this.messageDecodedBoveRepository = messageDecodedBoveRepository;
        this.messageDecodedYounioRepository = messageDecodedYounioRepository;
    }

    public List<Object> getLatestRecords() {
        List<MessageDecodedYounio> tableARecords = messageDecodedYounioRepository.findAllMessagesOrderedBySendAt();
        List<MessageDecodedBove> tableBRecords = messageDecodedBoveRepository.findAllMessagesOrderedBySendAt();
        
        List<Object> result = new ArrayList<>();
        result.addAll(tableARecords);
        result.addAll(tableBRecords);
        
        return result;
    }
}
