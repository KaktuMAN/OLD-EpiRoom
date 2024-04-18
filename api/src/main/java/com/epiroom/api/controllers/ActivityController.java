package com.epiroom.api.controllers;

import com.epiroom.api.model.Activity;
import com.epiroom.api.model.Campus;
import com.epiroom.api.model.dto.activity.FullActivity;
import com.epiroom.api.model.dto.activity.PaginatedActivity;
import com.epiroom.api.repository.ActivityRepository;
import com.epiroom.api.repository.CampusRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@Tag(name = "Activities", description = "Activity API")
@RestController
@RequestMapping("/activities")
public class ActivityController {
    CampusRepository campusRepository;
    ActivityRepository activityRepository;

    public ActivityController(CampusRepository campusRepository, ActivityRepository activityRepository) {
        this.campusRepository = campusRepository;
        this.activityRepository = activityRepository;
    }

    @GetMapping("/{campusCode}/{activityId}")
    @Operation(summary = "Get activity details", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
            @Parameter(name = "activityId", description = "The activity id", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Activity found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FullActivity.class))
            }),
            @ApiResponse(responseCode = "404", description = "Campus or Activity not found", content = @Content)
    })
    public ResponseEntity<FullActivity> getActivity(@PathVariable String campusCode, @PathVariable int activityId) {
        Activity activity = activityRepository.findByIdAndCampusCode(activityId, campusCode);
        if (activity == null || !activity.getCampusCode().equals(campusCode))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new FullActivity(activity));
    }

    @GetMapping("/{campusCode}")
    @Operation(summary = "Get all events of a campus (Paginated)", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
            @Parameter(name = "page", description = "The page number"),
            @Parameter(name = "entries", description = "The number of entries per page")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Events found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PaginatedActivity.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid page or entries (page < 1, entries < 1 or entries > 100)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Campus not found", content = @Content)
    })
    public ResponseEntity<PaginatedActivity> getEventsByCampusCode(@PathVariable String campusCode, @RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "50") int entries) {
        if (page < 1 || entries < 1 || entries > 100)
            return ResponseEntity.badRequest().build();
        Campus campus = campusRepository.findByCode(campusCode);
        if (campus == null)
            return ResponseEntity.notFound().build();
        List<Activity> activities = activityRepository.findAllByAndCampusCode(PageRequest.of(page - 1, entries), campusCode);
        return ResponseEntity.ok(new PaginatedActivity(page, activities.size(), activities.stream().map(FullActivity::new).toList()));
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
