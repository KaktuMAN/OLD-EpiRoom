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
@Tag(name = "Room", description = "Room API")
@RestController
@RequestMapping("/room")
public class RoomController {

    @GetMapping("/{campusCode}/")
    @PreAuthorize("hasAuthority('rooms:read') OR hasAuthority('rooms:*')")
    @Operation(summary = "Get all rooms of a campus", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rooms found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Campus not found")
    })
    public ResponseEntity<Void> getRoomsByCampusCode(@PathVariable String campusCode) {
        return ResponseEntity.status(501).build();
    }

    @PostMapping("/{campusCode}/")
    @PreAuthorize("hasAuthority('rooms:write') OR hasAuthority('rooms:*')")
    @Operation(summary = "Create a room", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Room created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Campus not found")
    })
    public ResponseEntity<Void> createRoom(@PathVariable String campusCode) {
        return ResponseEntity.status(501).build();
    }

    @PutMapping("/{campusCode}/{roomCode}/")
    @PreAuthorize("hasAuthority('rooms:write') OR hasAuthority('rooms:*')")
    @Operation(summary = "Update a room", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
            @Parameter(name = "roomCode", description = "The room code", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Room updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Campus or Room not found")
    })
    public ResponseEntity<Void> updateRoom(@PathVariable String campusCode, @PathVariable String roomCode) {
        return ResponseEntity.status(501).build();
    }

    @DeleteMapping("/{campusCode}/{roomCode}/")
    @PreAuthorize("hasAuthority('rooms:write') OR hasAuthority('rooms:*')")
    @Operation(summary = "Delete a room", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
            @Parameter(name = "roomCode", description = "The room code", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Room deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Campus or Room not found")
    })
    public ResponseEntity<Void> deleteRoom(@PathVariable String campusCode, @PathVariable String roomCode) {
        return ResponseEntity.status(501).build();
    }
}
