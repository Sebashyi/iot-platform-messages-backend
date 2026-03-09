package com.m3verificaciones.appweb.messages.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.m3verificaciones.appweb.messages.exception.MessageUpdateException;
import com.m3verificaciones.appweb.messages.model.MessageDecodedBove;
import com.m3verificaciones.appweb.messages.repository.MessageDecodedBoveRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MessageDecodedBoveService {
    private final MessageDecodedBoveRepository messageDecodedRepository;

    public MessageDecodedBoveService(MessageDecodedBoveRepository messageDecodedRepository) {
        this.messageDecodedRepository = messageDecodedRepository;
    }

    @Transactional
    public MessageDecodedBove createMessage(MessageDecodedBove messages) {
        return messageDecodedRepository.save(messages);
    }

    @Transactional(readOnly = true)
    public List<MessageDecodedBove> getAllMessages() {
        return messageDecodedRepository.findAllMessagesOrderedBySendAt();
    }

    @Transactional(readOnly = true)
    public Optional<MessageDecodedBove> getMessageById(String id) {
        return messageDecodedRepository.findById(id);
    }

    public MessageDecodedBove updateMessage(MessageDecodedBove updatedMessage) throws MessageUpdateException {
        return messageDecodedRepository.save(updatedMessage);
    }

    @Transactional
    public void deleteMessage(String id) {
        MessageDecodedBove message = messageDecodedRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Message not found with id " + id));
        messageDecodedRepository.delete(message);
    }

    @Transactional
    public List<MessageDecodedBove> getAllMessagesDecodedByDevEui(String devEui) {
        return messageDecodedRepository.findAllByDevEuiOrderBySendMessageTimeDesc(devEui);
    }
}
