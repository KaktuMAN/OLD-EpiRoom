package com.epiroom.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000, https://epiroom.pechart.fr")
@Tag(name = "Messages", description = "Message API")
@RestController
@RequestMapping("/messages")
public class MessageController {
    @GetMapping("/{campusCode}/")
    @PreAuthorize("hasAuthority('messages:read') OR hasAuthority('messages:*')")
    @Operation(summary = "Get all messages of a campus", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Messages found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Campus not found")
    })
    public ResponseEntity<Void> getMessagesByCampusCode(@PathVariable String campusCode) {
        return ResponseEntity.status(501).build();
    }

    @PostMapping("/{campusCode}/")
    @PreAuthorize("hasAuthority('messages:write') OR hasAuthority('messages:*')")
    @Operation(summary = "Create a message", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Message created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Campus not found")
    })
    public ResponseEntity<Void> createMessage(@PathVariable String campusCode) {
        return ResponseEntity.status(501).build();
    }

    @PutMapping("/{campusCode}/{messageId}/")
    @PreAuthorize("hasAuthority('messages:write') OR hasAuthority('messages:*')")
    @Operation(summary = "Update a message", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
            @Parameter(name = "messageId", description = "The message id", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Message updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Campus or Message not found")
    })
    public ResponseEntity<Void> updateMessage(@PathVariable String campusCode, @PathVariable String messageId) {
        return ResponseEntity.status(501).build();
    }

    @DeleteMapping("/{campusCode}/{messageId}/")
    @PreAuthorize("hasAuthority('messages:write') OR hasAuthority('messages:*')")
    @Operation(summary = "Delete a message", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
            @Parameter(name = "messageId", description = "The message id", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Message deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Campus or Message not found")
    })
    public ResponseEntity<Void> deleteMessage(@PathVariable String campusCode, @PathVariable String messageId) {
        return ResponseEntity.status(501).build();
    }
}
