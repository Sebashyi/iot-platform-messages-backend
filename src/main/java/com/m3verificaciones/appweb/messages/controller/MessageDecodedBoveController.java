package com.m3verificaciones.appweb.messages.controller;

import com.m3verificaciones.appweb.messages.model.MessageDecodedBove;
import com.m3verificaciones.appweb.messages.service.MessageDecodedBoveService;
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
@RequestMapping("/m3verificaciones/api/v1/message-meter-bove")
@Tag(name = "Message Decoded Bove", description = "API for managing decoded Bove meter messages")
public class MessageDecodedBoveController {
    private final MessageDecodedBoveService messageDecodedService;

    public MessageDecodedBoveController(MessageDecodedBoveService messageDecodedService) {
        this.messageDecodedService = messageDecodedService;
    }

    @Operation(summary = "Get all messages", description = "Retrieves all decoded Bove messages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDecodedBove.class)) }),
            @ApiResponse(responseCode = "404", description = "No messages found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Object> getAllMessages() {
        try {
            List<MessageDecodedBove> messages = messageDecodedService.getAllMessages();
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
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error retrieving messages");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get message by ID", description = "Retrieves a decoded Bove message by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDecodedBove.class)) }),
            @ApiResponse(responseCode = "404", description = "Message not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getMessageById(
            @Parameter(description = "ID of message to retrieve") @PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            return messageDecodedService.getMessageById(id)
                    .map(message -> new ResponseEntity<Object>(message, HttpStatus.OK))
                    .orElseGet(() -> {
                        response.put("message", "Message with ID: " + id + " not found");
                        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                    });
        } catch (DataAccessException e) {
            response.put("message", "Error retrieving message with ID: " + id);
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            response.put("message", "Error retrieving message");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Create message", description = "Creates a new decoded Bove message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Message created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDecodedBove.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid message data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Object> createMessage(
            @Parameter(description = "Message to create") @Valid @RequestBody MessageDecodedBove message) {
        Map<String, Object> response = new HashMap<>();
        try {
            MessageDecodedBove createdMessage = messageDecodedService.createMessage(message);
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

    @Operation(summary = "Update message", description = "Updates an existing decoded Bove message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDecodedBove.class)) }),
            @ApiResponse(responseCode = "404", description = "Message not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(
            @Parameter(description = "ID of message to update") @Valid @PathVariable String id,
            @Parameter(description = "Updated message data") @RequestBody MessageDecodedBove message) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Check if message exists
            if (!messageDecodedService.getMessageById(id).isPresent()) {
                response.put("message", "Message with ID: " + id + " not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            message.setUniqueKey(id);
            MessageDecodedBove savedMessage = messageDecodedService.updateMessage(message);
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

    @Operation(summary = "Delete message", description = "Deletes a decoded Bove message by its unique identifier")
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
            if (!messageDecodedService.getMessageById(id).isPresent()) {
                response.put("message", "Message with ID: " + id + " not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            messageDecodedService.deleteMessage(id);
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

    @Operation(summary = "Get messages by DevEui", description = "Retrieves all decoded Bove messages for a specific device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDecodedBove.class)) }),
            @ApiResponse(responseCode = "404", description = "No messages found for the provided DevEui", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/dev-eui/{devEui}")
    public ResponseEntity<Object> getAllMessagesDecodedByDevEui(
            @Parameter(description = "DevEui of the device") @PathVariable String devEui) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<MessageDecodedBove> messagesDecoded = messageDecodedService.getAllMessagesDecodedByDevEui(devEui);
            if (messagesDecoded.isEmpty()) {
                response.put("message", "No messages found for DevEui: " + devEui);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(messagesDecoded, HttpStatus.OK);
        } catch (DataAccessException e) {
            response.put("message", "Error retrieving messages for DevEui: " + devEui);
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            response.put("message", "Error processing request");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}