package com.m3verificaciones.appweb.messages.controller;

import com.m3verificaciones.appweb.messages.model.Messages;
import com.m3verificaciones.appweb.messages.service.MessagesService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST,
        RequestMethod.PUT, RequestMethod.DELETE })
@RestController
@RequestMapping("/m3verificaciones/api/v1/messages")
@Tag(name = "Messages", description = "API for managing raw IoT messages")
public class MessagesController {
    private final MessagesService messagesService;

    public MessagesController(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @Operation(summary = "Get all messages", description = "Retrieves all raw IoT messages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Messages.class)) }),
            @ApiResponse(responseCode = "404", description = "No messages found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Object> getAllMessages() {
        try {
            List<Messages> messages = messagesService.getAllMessages();
            if (messages.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "No messages found in database");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (DataAccessException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error retrieving messages from database");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get message by ID", description = "Retrieves a message by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Messages.class)) }),
            @ApiResponse(responseCode = "404", description = "Message not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getMessageById(
            @Parameter(description = "ID of message to retrieve") @PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            return messagesService.getMessageById(id)
                    .map(message -> new ResponseEntity<Object>(message, HttpStatus.OK))
                    .orElseGet(() -> {
                        response.put("message", "Message with ID: " + id + " not found");
                        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                    });
        } catch (DataAccessException e) {
            response.put("message", "Error retrieving message with ID: " + id);
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get all messages in descending order", description = "Retrieves all messages sorted by creation date in descending order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Messages.class)) }),
            @ApiResponse(responseCode = "404", description = "No messages found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/desc")
    public ResponseEntity<Object> getAllMessagesInDescendingOrder() {
        try {
            List<Messages> messages = messagesService.getAllMessagesByDesc();
            if (messages.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "No messages found in database");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (DataAccessException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error retrieving messages from database");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Find messages by device serials", description = "Retrieves messages filtered by device serial numbers, ordered by creation date descending")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Messages.class)) }),
            @ApiResponse(responseCode = "404", description = "No messages found for the provided serial numbers", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/by-serials")
    public ResponseEntity<Object> findMessagesBySerials(
            @Parameter(description = "List of device serial numbers") @RequestBody List<String> serials) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Messages> messages = messagesService.findMessagesBySerialsSortedByCreatedAtDesc(serials);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            response.put("message", "No messages found for the provided serial numbers");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.put("message", "Error retrieving messages");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Create message", description = "Creates a new raw IoT message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Message created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Messages.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid message data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Object> createMessage(
            @Parameter(description = "Message to create") @Valid @RequestBody Messages message) {
        Map<String, Object> response = new HashMap<>();
        try {
            Messages createdMessage = messagesService.createMessage(message);
            return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            response.put("message", "Error saving message to database");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            response.put("message", "Error processing request");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Update message", description = "Updates an existing raw IoT message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Messages.class)) }),
            @ApiResponse(responseCode = "404", description = "Message not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(
            @Parameter(description = "ID of message to update") @Valid @PathVariable String id,
            @Parameter(description = "Updated message data") @RequestBody Messages message) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Check if message exists
            if (!messagesService.getMessageById(id).isPresent()) {
                response.put("message", "Message with ID: " + id + " not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            message.setUniqueKey(id);
            Messages savedMessage = messagesService.updateMessage(message);
            return new ResponseEntity<>(savedMessage, HttpStatus.OK);
        } catch (DataAccessException e) {
            response.put("message", "Error updating message in database");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            response.put("message", "Error processing request");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete message", description = "Deletes a raw IoT message by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message deleted successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)) }),
            @ApiResponse(responseCode = "404", description = "Message not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMessage(
            @Parameter(description = "ID of message to delete") @PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Check if the message exists before attempting to delete
            if (!messagesService.getMessageById(id).isPresent()) {
                response.put("message", "Message with ID: " + id + " not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            messagesService.deleteMessage(id);
            response.put("message", "Message deleted successfully");
            response.put("deleted", true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataAccessException e) {
            response.put("message", "Error deleting message from database");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            response.put("message", "Error processing delete request");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}