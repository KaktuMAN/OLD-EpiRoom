package com.epiroom.api.controllers;

import com.epiroom.api.model.Campus;
import com.epiroom.api.model.Floor;
import com.epiroom.api.model.Room;
import com.epiroom.api.model.dto.campus.FullCampus;
import com.epiroom.api.model.dto.campus.SimpleCampus;
import com.epiroom.api.model.dto.floor.CampusFloor;
import com.epiroom.api.model.dto.floor.SimpleFloor;
import com.epiroom.api.model.dto.room.FullRoom;
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

    @GetMapping("/{campusCode}/floors/main")
    @Operation(summary = "Get the main floor of a campus", parameters = {
            @Parameter(name = "campusCode", description = "Campus code", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CampusFloor.class))
            }),
            @ApiResponse(responseCode = "404", description = "Campus not found", content = @Content)
    })
    public ResponseEntity<CampusFloor> getMainFloor(@PathVariable String campusCode) {
        Campus campus = campusRepository.findByCode(campusCode);
        if (campus == null)
            return ResponseEntity.notFound().build();
        Floor mainFloor = campus.getFloors().stream().filter(Floor::isMainFloor).findFirst().orElse(null);
        if (mainFloor == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new CampusFloor(mainFloor));
    }

    @GetMapping("/{campusCode}/floors/{floor}/rooms")
    @Operation(summary = "Get all rooms of a campus floor", parameters = {
            @Parameter(name = "campusCode", description = "Campus code", required = true),
            @Parameter(name = "floor", description = "Floor number", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FullRoom.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Campus / Floor not found", content = @Content)
    })
    public ResponseEntity<List<FullRoom>> getCampusFloorRooms(@PathVariable String campusCode, @PathVariable int floor) {
        Campus campus = campusRepository.findByCode(campusCode);
        if (campus == null)
            return ResponseEntity.notFound().build();
        Floor campusFloor = floorRepository.findByCampusCodeAndFloor(campusCode, floor);
        if (campusFloor == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(campusFloor.getRooms().stream().map(FullRoom::new).toList());
    }

    @PutMapping("/{campusCode}/floors/{floor}/rooms")
    @Operation(summary = "Update a room of a campus floor", parameters = {
            @Parameter(name = "campusCode", description = "Campus code", required = true),
            @Parameter(name = "floor", description = "Floor number", required = true)
    }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = FullRoom.class))))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Room updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FullRoom.class))
            }),
            @ApiResponse(responseCode = "404", description = "Campus / Floor not found", content = @Content)
    })
    public ResponseEntity<FullRoom> updateRoom(@PathVariable String campusCode, @PathVariable int floor, @RequestBody FullRoom room) {
        Campus campus = campusRepository.findByCode(campusCode);
        if (campus == null)
            return ResponseEntity.notFound().build();
        Floor campusFloor = floorRepository.findByCampusCodeAndFloor(campusCode, floor);
        if (campusFloor == null)
            return ResponseEntity.notFound().build();
        Room previousRoom = roomRepository.findByCampusAndFloorAndCode(campus, campusFloor, room.getCode());
        if (previousRoom == null)
            previousRoom = new Room(room);
        else
            previousRoom.update(room);
        roomRepository.save(previousRoom);
        return ResponseEntity.ok(new FullRoom(previousRoom));
    }
}
