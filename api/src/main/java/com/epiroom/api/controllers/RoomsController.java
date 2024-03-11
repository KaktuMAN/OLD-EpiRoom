package com.epiroom.api.controllers;

import java.util.Comparator;
import java.util.List;

import com.epiroom.api.model.Campus;
import com.epiroom.api.model.Room;
import com.epiroom.api.repository.CampusRepository;
import com.epiroom.api.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RoomsController {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private CampusRepository campusRepository;

	@GetMapping("/rooms/{campusCode}")
        public ResponseEntity<List<Room>> getRooms(@PathVariable String campusCode) {
        Campus campus = campusRepository.findByCode(campusCode);
        List<Room> rooms = roomRepository.findByCampusCode(campusCode);
        if (campus == null) {
            return ResponseEntity.notFound().build();
        }
        rooms.sort(Comparator.comparingInt(Room::getId));
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/rooms/{campusCode}/{roomCode}")
    public ResponseEntity<Room> getRoom(@PathVariable String campusCode, @PathVariable String roomCode) {
        Room room = roomRepository.findByCampusCodeAndCode(campusCode, roomCode);
        if (room == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(room);
    }
}