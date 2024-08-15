package com.epiroom.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000, https://epiroom.pechart.fr")
@Tag(name = "Campus", description = "Campus API")
@RestController
@RequestMapping("/campus")
public class CampusController {
    @GetMapping("/")
    @Operation(summary = "Get all campuses")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Campuses found", content = @Content)
    })
    public ResponseEntity<Void> getCampuses() {
        return ResponseEntity.status(501).build();
    }

    @GetMapping("/{campusCode}")
    @Operation(summary = "Get a campus by code", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Campus found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Campus not found", content = @Content)
    })
    public ResponseEntity<Void> getCampusByCode(@PathVariable String campusCode) {
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/{campusCode}/")
    @PreAuthorize("hasAuthority('campus:write') OR hasAuthority('campus:*')")
    @Operation(summary = "Create a campus", parameters = {
            @Parameter(name = "code", description = "The campus code", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Campus created", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "409", description = "Campus already exists", content = @Content)
    })
    public ResponseEntity<Void> createCampus(@PathVariable String campusCode) {
        return ResponseEntity.status(501).build();
    }

    @PutMapping("/{campusCode}/")
    @PreAuthorize("hasAuthority('campus:write') OR hasAuthority('campus:*')")
    @Operation(summary = "Update a campus", parameters = {
            @Parameter(name = "code", description = "The campus code", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Campus updated", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Campus not found", content = @Content)
    })
    public ResponseEntity<Void> updateCampus(@PathVariable String campusCode) {
        return ResponseEntity.status(501).build();
    }

    @DeleteMapping("/{campusCode}/")
    @PreAuthorize("hasAuthority('campus:write') OR hasAuthority('campus:*')")
    @Operation(summary = "Delete a campus", parameters = {
            @Parameter(name = "code", description = "The campus code", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Campus deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Campus not found", content = @Content)
    })
    public ResponseEntity<Void> deleteCampus(@PathVariable String campusCode) {
        return ResponseEntity.status(501).build();
    }
}
