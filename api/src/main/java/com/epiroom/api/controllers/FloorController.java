package com.epiroom.api.controllers;

import com.epiroom.api.model.Floor;
import com.epiroom.api.repository.FloorRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000, https://epiroom.pechart.fr")
@Tag(name = "Floors", description = "Floors API")
@RestController
@RequestMapping("/floors")
public class FloorController {
    private final FloorRepository floorRepository;

    public FloorController(FloorRepository floorRepository) {
        this.floorRepository = floorRepository;
    }

    @GetMapping("/{campusCode}/{floorNumber}/svg")
    @Operation(summary = "Get the SVG of a campus", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
            @Parameter(name = "floorNumber", description = "The floor number", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SVG found", content = @Content(mediaType = "image/svg+xml")),
            @ApiResponse(responseCode = "404", description = "Campus not found")
    })
    public ResponseEntity<String> getSvgByCampusCode(@PathVariable String campusCode, @PathVariable int floorNumber) {
        List<Floor> floors = floorRepository.findAllByCampusCode(campusCode);
        Floor floor = floors.stream().filter(f -> f.getFloor() == floorNumber).findFirst().orElse(null);
        if (floor == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(floor.getSvg());
    }

    @GetMapping("/{campusCode}")
    @PreAuthorize("hasAuthority('floors:read') OR hasAuthority('floors:*')")
    @Operation(summary = "Get all floors of a campus", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Floors found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Campus not found")
    })
    public ResponseEntity<Void> getFloorsByCampusCode(@PathVariable String campusCode) {
        return ResponseEntity.status(501).build();
    }

    @PostMapping("/{campusCode}")
    @PreAuthorize("hasAuthority('floors:write') OR hasAuthority('floors:*')")
    @Operation(summary = "Create a floor", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Floor created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Floor already exists")
    })
    public ResponseEntity<Void> createFloor(@PathVariable String campusCode) {
        return ResponseEntity.status(501).build();
    }

    @PutMapping("/{campusCode}/{floorId}")
    @PreAuthorize("hasAuthority('floors:write') OR hasAuthority('floors:*')")
    @Operation(summary = "Update a floor", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
            @Parameter(name = "floorId", description = "The floor id", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Floor updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Campus or Floor not found")
    })
    public ResponseEntity<Void> updateFloor(@PathVariable String campusCode, @PathVariable String floorId) {
        return ResponseEntity.status(501).build();
    }

    @DeleteMapping("/{campusCode}/{floorId}")
    @PreAuthorize("hasAuthority('floors:write') OR hasAuthority('floors:*')")
    @Operation(summary = "Delete a floor", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
            @Parameter(name = "floorId", description = "The floor id", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Floor deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Campus or Floor not found")
    })
    public ResponseEntity<Void> deleteFloor(@PathVariable String campusCode, @PathVariable String floorId) {
        return ResponseEntity.status(501).build();
    }
}
