package com.epiroom.api.controllers;


import com.epiroom.api.repository.ActivityRepository;
import com.epiroom.api.model.dto.activity.PaginatedActivity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
