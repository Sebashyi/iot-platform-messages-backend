package com.m3verificaciones.appweb.messages.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;

import com.m3verificaciones.appweb.messages.service.FullJoinService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE
})
@RestController
@RequestMapping("/m3verificaciones/api/v1/messages-full-join")
@Tag(name = "Messages Full Join", description = "API to retrieve joined message records")
public class FullJoinController {
    private final FullJoinService dataService;

    public FullJoinController(FullJoinService dataService) {
        this.dataService = dataService;
    }

    @Operation(summary = "Get latest records", description = "Retrieves the most recent message records from the join operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved records", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "No records found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/latest")
    public ResponseEntity<Object> getLatestRecords() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Object> records = dataService.getLatestRecords();

            if (records.isEmpty()) {
                response.put("message", "No records found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (DataAccessException e) {
            response.put("message", "Error retrieving records from database");
            response.put("error", e.getMessage() + ": " + e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            response.put("message", "Error retrieving records");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}