package com.epiroom.api.controllers;

import com.epiroom.api.model.Campus;
import com.epiroom.api.model.dto.campus.FullCampus;
import com.epiroom.api.model.dto.campus.SimpleCampus;
import com.epiroom.api.model.dto.floor.CampusFloor;
import com.epiroom.api.repository.CampusRepository;
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

    public CampusController(CampusRepository campusRepository) {
        this.campusRepository = campusRepository;
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
}
