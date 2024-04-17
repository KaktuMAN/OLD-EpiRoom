package com.epiroom.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@Tag(name = "Activities", description = "Activity API")
@RestController
@RequestMapping("/activities")
public class ActivityController {
    @GetMapping("/{campusCode}")
    @PreAuthorize("hasAuthority('activities:read') OR hasAuthority('activities:*')")
    @Operation(summary = "Get all activities of a campus", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Activities found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Campus not found")
    })
    public ResponseEntity<Void> getActivitiesByCampusCode(@PathVariable String campusCode) {
        return ResponseEntity.status(501).build();
    }

    @PostMapping("/{campusCode}")
    @PreAuthorize("hasAuthority('activities:write') OR hasAuthority('activities:*')")
    @Operation(summary = "Create an activity", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Activity created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Activity already exists")
    })
    public ResponseEntity<Void> createActivity(@PathVariable String campusCode) {
        return ResponseEntity.status(501).build();
    }

    @PostMapping("/{campusCode}/bulk")
    @PreAuthorize("hasAuthority('activities:write') OR hasAuthority('activities:*')")
    @Operation(summary = "Create multiple activities", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Activities created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Activity already exists")
    })
    public ResponseEntity<Void> createActivities(@PathVariable String campusCode) {
        return ResponseEntity.status(501).build();
    }

    @PutMapping("/{campusCode}/{activityId}")
    @PreAuthorize("hasAuthority('activities:write') OR hasAuthority('activities:*')")
    @Operation(summary = "Update an activity", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
            @Parameter(name = "activityId", description = "The activity id", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Activity updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Campus or Activity not found")
    })
    public ResponseEntity<Void> updateActivity(@PathVariable String campusCode, @PathVariable String activityId) {
        return ResponseEntity.status(501).build();
    }

    @DeleteMapping("/{campusCode}/{activityId}")
    @PreAuthorize("hasAuthority('activities:delete') OR hasAuthority('activities:*')")
    @Operation(summary = "Delete an activity", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
            @Parameter(name = "activityId", description = "The activity id", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Activity deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Campus or Activity not found")
    })
    public ResponseEntity<Void> deleteActivity(@PathVariable String campusCode, @PathVariable String activityId) {
        return ResponseEntity.status(501).build();
    }
}
