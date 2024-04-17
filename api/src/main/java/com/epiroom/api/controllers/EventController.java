package com.epiroom.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@Tag(name = "Events", description = "Event API")
@RestController
@RequestMapping("/events")
public class EventController {
    @GetMapping("/{campusCode}/today")
    @Operation(summary = "Get all events of a campus for today", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Events found"),
            @ApiResponse(responseCode = "404", description = "Campus not found")
    })
    public ResponseEntity<Void> getEventsByCampusCodeToday(@PathVariable String campusCode) {
        return ResponseEntity.status(501).build();
    }

    @GetMapping("/{campusCode}")
    @PreAuthorize("hasAuthority('events:read') OR hasAuthority('events:*')")
    @Operation(summary = "Get all events of a campus", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Events found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Campus not found")
    })
    public ResponseEntity<Void> getEventsByCampusCode(@PathVariable String campusCode) {
        return ResponseEntity.status(501).build();
    }

    @PostMapping("/{campusCode}")
    @PreAuthorize("hasAuthority('events:write') OR hasAuthority('events:*')")
    @Operation(summary = "Create an event", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Event created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Campus not found")
    })
    public ResponseEntity<Void> createEvent(@PathVariable String campusCode) {
        return ResponseEntity.status(501).build();
    }

    @PutMapping("/{campusCode}/{eventId}")
    @PreAuthorize("hasAuthority('events:write') OR hasAuthority('events:*')")
    @Operation(summary = "Update an event", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
            @Parameter(name = "eventId", description = "The event id", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Campus or Event not found")
    })
    public ResponseEntity<Void> updateEvent(@PathVariable String campusCode, @PathVariable String eventId) {
        return ResponseEntity.status(501).build();
    }

    @DeleteMapping("/{campusCode}/{eventId}")
    @PreAuthorize("hasAuthority('events:write') OR hasAuthority('events:*')")
    @Operation(summary = "Delete an event", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
            @Parameter(name = "eventId", description = "The event id", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Campus or Event not found")
    })
    public ResponseEntity<Void> deleteEvent(@PathVariable String campusCode, @PathVariable String eventId) {
        return ResponseEntity.status(501).build();
    }
}
