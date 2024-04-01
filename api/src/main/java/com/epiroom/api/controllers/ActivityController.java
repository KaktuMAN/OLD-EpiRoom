package com.epiroom.api.controllers;


import com.epiroom.api.model.Activity;
import com.epiroom.api.model.Campus;
import com.epiroom.api.model.Event;
import com.epiroom.api.model.dto.activity.FullActivity;
import com.epiroom.api.model.dto.event.FullEvent;
import com.epiroom.api.repository.ActivityRepository;
import com.epiroom.api.model.dto.activity.PaginatedActivity;
import com.epiroom.api.repository.CampusRepository;
import com.epiroom.api.repository.EventRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Tag(name = "Activity", description = "Activity API")
@RestController
@RequestMapping("/activity")
public class ActivityController {
    private final ActivityRepository activityRepository;
    private final EventRepository eventRepository;
    private final CampusRepository campusRepository;

    public ActivityController(ActivityRepository activityRepository, EventRepository eventRepository, CampusRepository campusRepository) {
        this.activityRepository = activityRepository;
        this.eventRepository = eventRepository;
        this.campusRepository = campusRepository;
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

    @GetMapping("/today")
    @Operation(summary = "Get activities of today", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Activities found", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FullEvent.class))
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Invalid campus code", content = @Content)
    })
    public ResponseEntity<List<FullEvent>> getTodayActivities(@RequestParam String campusCode) {
        Date start = Date.from(LocalDateTime.of(LocalDate.now(), LocalTime.MIN).toInstant(java.time.ZoneOffset.UTC));
        Date end = Date.from(LocalDateTime.of(LocalDate.now(), LocalTime.MAX).toInstant(java.time.ZoneOffset.UTC));
        Campus campus = campusRepository.findByCode(campusCode);
        if (campus == null)
            return ResponseEntity.badRequest().build();
        List<Event> events = eventRepository.findAllByStartAfterAndEndBeforeAndCampusCode(start, end, campusCode);
        List<Event> newEvents = new ArrayList<>();
        Iterator<Event> iterator = events.iterator();
        while (iterator.hasNext()) {
            Event event = iterator.next();
            if (event.getRoom() != null && event.getRoom().getLinkedRooms() != null) {
                event.getRoom().getLinkedRooms().forEach(room -> {
                    Event linkedEvent = new Event(event);
                    linkedEvent.setRoom(room);
                    newEvents.add(linkedEvent);
                });
                iterator.remove();
            }
        }
        events.addAll(newEvents);
        return ResponseEntity.ok(events.stream().map(FullEvent::new).toList());
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
