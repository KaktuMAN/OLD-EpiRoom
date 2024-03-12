package com.epiroom.api.controllers;

import com.epiroom.api.model.Campus;
import com.epiroom.api.model.Room;
import com.epiroom.api.openapi.campus.PatchExistingCampus;
import com.epiroom.api.openapi.campus.PostNewCampus;
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
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Campus.class)))
            })
    })
    public ResponseEntity<List<Campus>> getCampuses() {
        return ResponseEntity.ok(campusRepository.findAll());
    }

    @PutMapping("/")
    @Operation(summary = "Add a new campus", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = PostNewCampus.class))))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Campus added", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Campus.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid campus", content = @Content)
    })
    public ResponseEntity<Campus> addCampus(@RequestBody PostNewCampus campus) {
        Campus existingCampus = campusRepository.findByCode(campus.getCode());
        if (existingCampus != null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(campusRepository.save(new Campus(campus)));
    }

    @GetMapping("/{code}")
    @Operation(summary = "Get a campus by code", parameters = {
            @Parameter(name = "code", description = "Campus code", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Campus.class))
            }),
            @ApiResponse(responseCode = "404", description = "Campus not found", content = @Content)
    })
    public ResponseEntity<Campus> getCampusByCode(@PathVariable String code) {
        Campus campus = campusRepository.findByCode(code);
        if (campus == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(campus);
    }

    @DeleteMapping("/{code}")
    @Operation(summary = "Delete a campus by code", parameters = {
            @Parameter(name = "code", description = "Campus code", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Campus deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Campus not found", content = @Content)
    })
    public ResponseEntity<Campus> deleteCampusByCode(@PathVariable String code) {
        Campus campus = campusRepository.findByCode(code);
        if (campus == null)
            return ResponseEntity.notFound().build();
        campusRepository.delete(campus);
        return ResponseEntity.ok(campus);
    }

    @PatchMapping("/{code}")
    @Operation(summary = "Update a campus by code", parameters = {
            @Parameter(name = "code", description = "Campus code", required = true)
    }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = PatchExistingCampus.class))))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Campus updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Campus.class))
            }),
            @ApiResponse(responseCode = "404", description = "Campus not found", content = @Content)
    })
    public ResponseEntity<Campus> updateCampusByCode(@PathVariable String code, @RequestBody PatchExistingCampus campus) {
        Campus existingCampus = campusRepository.findByCode(code);
        if (existingCampus == null)
            return ResponseEntity.notFound().build();
        existingCampus.setName(campus.getName());
        existingCampus.setMainFloorId(campus.getMainFloorId());
        campusRepository.save(existingCampus);
        return ResponseEntity.ok(existingCampus);
    }
}
