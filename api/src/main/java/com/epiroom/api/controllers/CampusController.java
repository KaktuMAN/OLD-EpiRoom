package com.epiroom.api.controllers;

import com.epiroom.api.model.Campus;
import com.epiroom.api.model.Floor;
import com.epiroom.api.model.Room;
import com.epiroom.api.model.dto.campus.FullCampus;
import com.epiroom.api.model.dto.campus.SimpleCampus;
import com.epiroom.api.model.dto.floor.CampusFloor;
import com.epiroom.api.model.dto.floor.SimpleFloor;
import com.epiroom.api.model.dto.room.FullRoom;
import com.epiroom.api.model.dto.room.LinkedRoom;
import com.epiroom.api.repository.CampusRepository;
import com.epiroom.api.repository.FloorRepository;
import com.epiroom.api.repository.RoomRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000, https://epiroom.pechart.fr")
@Tag(name = "Campus", description = "Campus API")
@RestController
@RequestMapping("/campus")
public class CampusController {
    private final CampusRepository campusRepository;
    private final FloorRepository floorRepository;
    private final RoomRepository roomRepository;

    public CampusController(CampusRepository campusRepository, FloorRepository floorRepository, RoomRepository roomRepository) {
        this.campusRepository = campusRepository;
        this.floorRepository = floorRepository;
        this.roomRepository = roomRepository;
    }

    @GetMapping("/")
    @Operation(summary = "Get all campuses")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SimpleCampus.class)))
            })
    })
    public ResponseEntity<List<SimpleCampus>> getCampuses() {
        List<Campus> campuses = campusRepository.findAll();
        return ResponseEntity.ok(campuses.stream().map(SimpleCampus::new).toList());
    }

    @PostMapping("/")
    @Operation(summary = "Add a new campus", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = SimpleCampus.class))))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Campus added", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SimpleCampus.class))
            }),
            @ApiResponse(responseCode = "400", description = "Campus already campus", content = @Content)
    })
    public ResponseEntity<SimpleCampus> addCampus(@RequestBody SimpleCampus campus) {
        Campus existingCampus = campusRepository.findByCode(campus.getCode());
        if (existingCampus != null)
            return ResponseEntity.badRequest().build();
        Campus newCampus = new Campus(campus);
        campusRepository.save(newCampus);
        return ResponseEntity.ok(new SimpleCampus(newCampus));
    }

    @GetMapping("/{campusCode}")
    @Operation(summary = "Get a campus by code", parameters = {
            @Parameter(name = "campusCode", description = "Campus code", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FullCampus.class))
            }),
            @ApiResponse(responseCode = "404", description = "Campus not found", content = @Content)
    })
    public ResponseEntity<FullCampus> getCampusByCode(@PathVariable String campusCode) {
        Campus campus = campusRepository.findByCode(campusCode);
        if (campus == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new FullCampus(campus));
    }

    @GetMapping("/{campusCode}/floors")
    @Operation(summary = "Get all floors from a campus", parameters = {
            @Parameter(name = "campusCode", description = "Campus code", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CampusFloor.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Campus not found", content = @Content)
    })
    public ResponseEntity<List<CampusFloor>> getCampusFloors(@PathVariable String campusCode) {
        Campus campus = campusRepository.findByCode(campusCode);
        if (campus == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(campus.getFloors().stream().map(CampusFloor::new).toList());
    }

    @PostMapping("/{campusCode}/floors")
    @Operation(summary = "Add a new floor to a campus", parameters = {
            @Parameter(name = "campusCode", description = "Campus code", required = true)
    }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = SimpleFloor.class))))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Floor added", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CampusFloor.class))
            }),
            @ApiResponse(responseCode = "400", description = "Floor already exists", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CampusFloor.class))
            }),
            @ApiResponse(responseCode = "404", description = "Campus not found", content = @Content)
    })
    public ResponseEntity<CampusFloor> addFloorToCampus(@PathVariable String campusCode, @RequestBody SimpleFloor floor) {
        Campus campus = campusRepository.findByCode(campusCode);
        if (campus == null)
            return ResponseEntity.notFound().build();
        if (floorRepository.existsByCampusCodeAndFloor(campus.getCode(), floor.getFloor()))
            return ResponseEntity.badRequest().body(new CampusFloor(campus, floor));
        Floor newFloor = floorRepository.save(new Floor(campus, floor.getFloor(), floor.getName()));
        return ResponseEntity.ok(new CampusFloor(newFloor));
    }

    @PatchMapping("/{campusCode}/floors/main")
    @Operation(summary = "Set the main floor of a campus", parameters = {
            @Parameter(name = "campusCode", description = "Campus code", required = true),
            @Parameter(name = "floor", description = "Floor number", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Main floor set", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CampusFloor.class))
            }),
            @ApiResponse(responseCode = "400", description = "Floor not found", content = @Content),
            @ApiResponse(responseCode = "404", description = "Campus not found", content = @Content)
    })
    public ResponseEntity<CampusFloor> setMainFloor(@PathVariable String campusCode, @RequestParam int floor) {
        Campus campus = campusRepository.findByCode(campusCode);
        if (campus == null)
            return ResponseEntity.notFound().build();
        Floor mainFloor = floorRepository.findByCampusCodeAndFloor(campus.getCode(), floor);
        if (mainFloor == null)
            return ResponseEntity.badRequest().build();
        campus.setMainFloorId(mainFloor.getId());
        campusRepository.save(campus);
        return ResponseEntity.ok(new CampusFloor(mainFloor));
    }

    @GetMapping("/{campusCode}/rooms")
    @Operation(summary = "Get all rooms of a campus floor", parameters = {
            @Parameter(name = "campusCode", description = "Campus code", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FullRoom.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Campus / Floor not found", content = @Content)
    })
    public ResponseEntity<List<FullRoom>> getCampusFloorRooms(@PathVariable String campusCode) {
        Campus campus = campusRepository.findByCode(campusCode);
        if (campus == null)
            return ResponseEntity.notFound().build();
        List<Room> rooms = roomRepository.findAllByCampusCode(campusCode);
        rooms.sort(Comparator.comparingInt(Room::getFloorCode));
        return ResponseEntity.ok(rooms.stream().map(FullRoom::new).toList());
    }

    @PutMapping("/{campusCode}/rooms")
    @Operation(summary = "Update a room of a campus floor", parameters = {
            @Parameter(name = "campusCode", description = "Campus code", required = true)
    }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = FullRoom.class))))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Room updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Campus / Floor not found", content = @Content)
    })
    public ResponseEntity<Void> updateRoom(@PathVariable String campusCode, @RequestBody FullRoom room) {
        Campus campus = campusRepository.findByCode(campusCode);
        if (campus == null)
            return ResponseEntity.notFound().build();
        Floor floor = floorRepository.findByCampusCodeAndFloor(campusCode, room.getFloor());
        if (floor == null)
            return ResponseEntity.notFound().build();
        Room previousRoom = roomRepository.findByCampusAndCode(campus, room.getCode());
        if (previousRoom == null)
            previousRoom = new Room(room, floor);
        else
            previousRoom.update(room, floor);
        roomRepository.save(previousRoom);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{campusCode}/rooms/link")
    @Operation(summary = "Link rooms together", parameters = {
            @Parameter(name = "campusCode", description = "Campus code", required = true)
    }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = LinkedRoom.class))))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rooms linked", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Campus / Room not found", content = @Content)
    })
    public ResponseEntity<Void> linkRooms(@PathVariable String campusCode, @RequestBody LinkedRoom linkedRoom) {
        Campus campus = campusRepository.findByCode(campusCode);
        if (campus == null)
            return ResponseEntity.notFound().build();
        Room mainRoom = roomRepository.findByCampusAndCode(campus, linkedRoom.getMainRoomCode());
        List<Room> linkedRooms = roomRepository.findAllByCampusCodeAndCodeIn(campusCode, linkedRoom.getLinkedRoomCodes());
        if (mainRoom == null || linkedRooms.size() != linkedRoom.getLinkedRoomCodes().size())
            return ResponseEntity.notFound().build();
        mainRoom.getLinkedRooms().clear();
        mainRoom.getLinkedRooms().addAll(linkedRooms);
        roomRepository.save(mainRoom);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{campusCode}/rooms/link")
    @Operation(summary = "Get linked rooms", parameters = {
            @Parameter(name = "campusCode", description = "Campus code", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LinkedRoom.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Campus / Room not found", content = @Content)
    })
    public ResponseEntity<List<LinkedRoom>> getLinkedRooms(@PathVariable String campusCode) {
        Campus campus = campusRepository.findByCode(campusCode);
        if (campus == null)
            return ResponseEntity.notFound().build();
        List<Room> rooms = roomRepository.findAllByCampusCode(campusCode);
        List<LinkedRoom> linkedRooms = rooms.stream().filter(room -> !room.getLinkedRooms().isEmpty()).map(LinkedRoom::new).toList();
        return ResponseEntity.ok(linkedRooms);
    }

    @DeleteMapping("/{campusCode}/rooms/link")
    @Operation(summary = "Unlink two rooms", parameters = {
            @Parameter(name = "campusCode", description = "Campus code", required = true),
            @Parameter(name = "room1", description = "Source room", required = true),
            @Parameter(name = "room2", description = "Room to unlink", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rooms unlinked", content = @Content),
            @ApiResponse(responseCode = "400", description = "Rooms not linked", content = @Content),
            @ApiResponse(responseCode = "404", description = "Campus / Room not found", content = @Content)
    })
    public ResponseEntity<Void> unlinkRooms(@PathVariable String campusCode, @RequestParam String room1, @RequestParam String room2) {
        Campus campus = campusRepository.findByCode(campusCode);
        if (campus == null)
            return ResponseEntity.notFound().build();
        Room room1Entity = roomRepository.findByCampusAndCode(campus, room1);
        Room room2Entity = roomRepository.findByCampusAndCode(campus, room2);
        if (room1Entity == null || room2Entity == null)
            return ResponseEntity.notFound().build();
        if (!room1Entity.getLinkedRooms().contains(room2Entity))
            return ResponseEntity.badRequest().build();
        room1Entity.getLinkedRooms().remove(room2Entity);
        roomRepository.save(room1Entity);
        return ResponseEntity.ok().build();
    }
}
