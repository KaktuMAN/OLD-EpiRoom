package com.epiroom.api.controllers;


import com.epiroom.api.model.Activity;
import com.epiroom.api.model.dto.activity.FullActivity;
import com.epiroom.api.model.dto.event.FullEvent;
import com.epiroom.api.repository.ActivityRepository;
import com.epiroom.api.model.dto.activity.PaginatedActivity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Activity", description = "Activity API")
@RestController
@RequestMapping("/activity")
public class ActivityController {
    private final ActivityRepository activityRepository;

    public ActivityController(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }
    @GetMapping("/")
    @Operation(summary = "Get 100 activities", parameters = {
            @Parameter(name = "page", description = "The page number"),
            @Parameter(name = "size", description = "The page size (Min 1, Max 100)")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Activities found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PaginatedActivity.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid page or size")
    })
    public ResponseEntity<PaginatedActivity> getActivities(@RequestParam(required = false, defaultValue = "1") Integer page, @RequestParam(required = false, defaultValue = "50") Integer size) {
        if (page < 1 || size < 1 || size > 100)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(new PaginatedActivity(page, activityRepository.findAllBy(PageRequest.of(page - 1, size))));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an activity by id", parameters = {
            @Parameter(name = "id", description = "The activity id", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Activity found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FullActivity.class))
            }),
            @ApiResponse(responseCode = "404", description = "Activity not found")
    })
    public ResponseEntity<FullActivity> getActivity(@PathVariable Integer id) {
        Activity activity = activityRepository.findById(id);
        if (activity == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new FullActivity(activity));
    }

    @GetMapping("/{id}/events")
    @Operation(summary = "Get events of an activity by id", parameters = {
            @Parameter(name = "id", description = "The activity id", required = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Events found", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FullEvent.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Activity not found")
    })
    public ResponseEntity<List<FullEvent>> getEvents(@PathVariable Integer id) {
        Activity activity = activityRepository.findById(id);
        if (activity == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(activity.getEvents().stream().map(FullEvent::new).toList());
    }
}
