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
@Tag(name = "Floors", description = "Floors API")
@RestController
@RequestMapping("/floors")
public class FloorController {
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
