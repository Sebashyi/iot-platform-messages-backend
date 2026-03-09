package com.m3verificaciones.appweb.messages.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.m3verificaciones.appweb.messages.exception.MessageUpdateException;
import com.m3verificaciones.appweb.messages.model.MessageDecodedYounio;
import com.m3verificaciones.appweb.messages.repository.MessageDecodedYounioRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MessageDecodedYounioService {
    private final MessageDecodedYounioRepository messageDecodedYounioRepository;

    public MessageDecodedYounioService(MessageDecodedYounioRepository messageDecodedYounioRepository) {
        this.messageDecodedYounioRepository = messageDecodedYounioRepository;
    }

    @Transactional
    public MessageDecodedYounio createMessage(MessageDecodedYounio messages) {
        return messageDecodedYounioRepository.save(messages);
    }

    @Transactional(readOnly = true)
    public List<MessageDecodedYounio> getAllMessages() {
        return messageDecodedYounioRepository.findAllMessagesOrderedBySendAt();
    }

    @Transactional(readOnly = true)
    public Optional<MessageDecodedYounio> getMessageById(String id) {
        return messageDecodedYounioRepository.findById(id);
    }

    public MessageDecodedYounio updateMessage(MessageDecodedYounio updatedMessage) throws MessageUpdateException {
        return messageDecodedYounioRepository.save(updatedMessage);
    }

    @Transactional
    public void deleteMessage(String id) {
        MessageDecodedYounio message = messageDecodedYounioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Message not found with id " + id));
        messageDecodedYounioRepository.delete(message);
    }

    @Transactional
    public List<MessageDecodedYounio> getAllMessagesDecodedByDevEui(String devEui) {
        return messageDecodedYounioRepository.findAllByDevEuiOrderBySendMessageTimeDesc(devEui);
    }
}
