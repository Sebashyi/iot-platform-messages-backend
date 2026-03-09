package com.m3verificaciones.appweb.messages.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.m3verificaciones.appweb.messages.exception.MessageUpdateException;
import com.m3verificaciones.appweb.messages.model.Messages;
import com.m3verificaciones.appweb.messages.repository.MessagesRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MessagesService {
    private final MessagesRepository messagesRepository;

    public MessagesService(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    @Transactional
    public Messages createMessage(Messages messages) {
        return messagesRepository.save(messages);
    }

    @Transactional(readOnly = true)
    public List<Messages> getAllMessages() {
        return (List<Messages>) messagesRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Messages> getMessageById(String id) {
        return messagesRepository.findById(id);
    }

    public Messages updateMessage(Messages updatedMessage) throws MessageUpdateException {
        return messagesRepository.save(updatedMessage);
    }

    @Transactional
    public void deleteMessage(String id) {
        Messages message = messagesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Message not found with id " + id));
        messagesRepository.delete(message);
    }

    @Transactional(readOnly = true)
    public List<Messages> getAllMessagesByDesc() {
        return messagesRepository.findAllMessagesOrderedByCreatedAt();
    }

    @Transactional(readOnly = true)
    public List<Messages> findMessagesBySerialsSortedByCreatedAtDesc(List<String> serials) {
        List<Messages> messages = messagesRepository.findBySerialMeterInOrderByCreatedAtDesc(serials);
        if (messages.isEmpty()) {
            throw new EntityNotFoundException("No messages found for the provided serial numbers");
        }
        return messages;
    }
}
