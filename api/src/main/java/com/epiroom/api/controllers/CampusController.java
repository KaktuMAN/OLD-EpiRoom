package com.epiroom.api.controllers;

import com.epiroom.api.model.Campus;
import com.epiroom.api.model.Floor;
import com.epiroom.api.model.dto.campus.FullCampus;
import com.epiroom.api.model.dto.campus.SimpleCampus;
import com.epiroom.api.model.dto.floor.CampusFloor;
import com.epiroom.api.model.dto.floor.SimpleFloor;
import com.epiroom.api.repository.CampusRepository;
import com.epiroom.api.repository.FloorRepository;
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

@Tag(name = "Campus", description = "Campus API")
@RestController
@RequestMapping("/campus")
public class CampusController {
    private final CampusRepository campusRepository;
    private final FloorRepository floorRepository;

    public CampusController(CampusRepository campusRepository, FloorRepository floorRepository) {
        this.campusRepository = campusRepository;
        this.floorRepository = floorRepository;
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

    @GetMapping("/{code}")
    @Operation(summary = "Get a campus by code", parameters = {
            @Parameter(name = "code", description = "Campus code", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FullCampus.class))
            }),
            @ApiResponse(responseCode = "404", description = "Campus not found", content = @Content)
    })
    public ResponseEntity<FullCampus> getCampusByCode(@PathVariable String code) {
        Campus campus = campusRepository.findByCode(code);
        if (campus == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new FullCampus(campus));
    }

    @GetMapping("/{code}/floors")
    @Operation(summary = "Get all floors from a campus", parameters = {
            @Parameter(name = "code", description = "Campus code", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CampusFloor.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Campus not found", content = @Content)
    })
    public ResponseEntity<List<CampusFloor>> getCampusFloors(@PathVariable String code) {
        Campus campus = campusRepository.findByCode(code);
        if (campus == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(campus.getFloors().stream().map(CampusFloor::new).toList());
    }

    @PostMapping("/{code}/floors")
    @Operation(summary = "Add a new floor to a campus", parameters = {
            @Parameter(name = "code", description = "Campus code", required = true)
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
    public ResponseEntity<CampusFloor> addFloorToCampus(@PathVariable String code, @RequestBody SimpleFloor floor) {
        Campus campus = campusRepository.findByCode(code);
        if (campus == null)
            return ResponseEntity.notFound().build();
        if (floorRepository.existsByCampusCodeAndFloor(campus.getCode(), floor.getFloor()))
            return ResponseEntity.badRequest().body(new CampusFloor(campus, floor));
        Floor newFloor = floorRepository.save(new Floor(campus, floor.getFloor(), floor.getName()));
        return ResponseEntity.ok(new CampusFloor(newFloor));
    }

    @PatchMapping("/{code}/floors/main")
    @Operation(summary = "Set the main floor of a campus", parameters = {
            @Parameter(name = "code", description = "Campus code", required = true),
            @Parameter(name = "floor", description = "Floor number", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Main floor set", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CampusFloor.class))
            }),
            @ApiResponse(responseCode = "400", description = "Floor not found", content = @Content),
            @ApiResponse(responseCode = "404", description = "Campus not found", content = @Content)
    })
    public ResponseEntity<CampusFloor> setMainFloor(@PathVariable String code, @RequestParam int floor) {
        Campus campus = campusRepository.findByCode(code);
        if (campus == null)
            return ResponseEntity.notFound().build();
        Floor mainFloor = floorRepository.findByCampusCodeAndFloor(campus.getCode(), floor);
        if (mainFloor == null)
            return ResponseEntity.badRequest().build();
        campus.setMainFloorId(mainFloor.getId());
        campusRepository.save(campus);
        return ResponseEntity.ok(new CampusFloor(mainFloor));
    }
}
