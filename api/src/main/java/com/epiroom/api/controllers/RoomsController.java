package com.epiroom.api.controllers;

import java.util.Comparator;
import java.util.List;

import com.epiroom.api.model.Campus;
import com.epiroom.api.model.Room;
import com.epiroom.api.repository.CampusRepository;
import com.epiroom.api.repository.RoomRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Rooms", description = "Rooms API")
@RestController
public class RoomsController {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private CampusRepository campusRepository;

	@GetMapping("/rooms/{campusCode}")
    @Operation(summary = "Get all rooms from a campus", parameters = {
            @Parameter(name = "campusCode", description = "Campus code", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema (implementation = Room.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Room not found", content = @Content)
    })
    public ResponseEntity<List<Room>> getRooms(@PathVariable String campusCode) {
        Campus campus = campusRepository.findByCode(campusCode);
        List<Room> rooms = roomRepository.findByCampusCode(campusCode);
        if (campus == null)
            return ResponseEntity.notFound().build();
        rooms.sort(Comparator.comparingInt(Room::getId));
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/rooms/{campusCode}/{roomCode}")
    @Operation(summary = "Get a room from a campus", parameters = {
            @Parameter(name = "campusCode", description = "Campus code", required = true),
            @Parameter(name = "roomCode", description = "Room code", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Room.class))
            }),
            @ApiResponse(responseCode = "404", description = "Room not found", content = @Content)
    })
    public ResponseEntity<Room> getRoom(@PathVariable String campusCode, @PathVariable String roomCode) {
        Room room = roomRepository.findByCampusCodeAndCode(campusCode, roomCode);
        if (room == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(room);
    }
}