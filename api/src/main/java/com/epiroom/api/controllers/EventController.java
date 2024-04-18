package com.epiroom.api.controllers;

import com.epiroom.api.model.Campus;
import com.epiroom.api.model.Event;
import com.epiroom.api.model.dto.event.FullEvent;
import com.epiroom.api.model.dto.event.PaginatedEvent;
import com.epiroom.api.repository.CampusRepository;
import com.epiroom.api.repository.EventRepository;
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
@Tag(name = "Events", description = "Event API")
@RestController
@RequestMapping("/events")
public class EventController {
    CampusRepository campusRepository;
    EventRepository eventRepository;

    public EventController(CampusRepository campusRepository, EventRepository eventRepository) {
        this.campusRepository = campusRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping("/{campusCode}/today")
    @Operation(summary = "Get all events of a campus for today", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Events found"),
            @ApiResponse(responseCode = "404", description = "Campus not found")
    })
    public ResponseEntity<Void> getEventsByCampusCodeToday(@PathVariable String campusCode) {
        return ResponseEntity.status(501).build();
    }

    @GetMapping("/{campusCode}")
    @Operation(summary = "Get all events of a campus (Paginated)", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
            @Parameter(name = "page", description = "The page number"),
            @Parameter(name = "entries", description = "The number of entries per page")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Events found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PaginatedEvent.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid page or entries (page < 1, entries < 1 or entries > 100)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Campus not found", content = @Content)
    })
    public ResponseEntity<PaginatedEvent> getEventsByCampusCode(@PathVariable String campusCode, @RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "50") int entries) {
        if (page < 1 || entries < 1 || entries > 100)
            return ResponseEntity.badRequest().build();
        Campus campus = campusRepository.findByCode(campusCode);
        if (campus == null)
            return ResponseEntity.notFound().build();
        List<Event> events = eventRepository.findAllByAndCampusCode(PageRequest.of(page - 1, entries), campusCode);
        return ResponseEntity.ok(new PaginatedEvent(page, events.size(), events.stream().map(FullEvent::new).toList()));
    }

    @PostMapping("/{campusCode}")
    @PreAuthorize("hasAuthority('events:write') OR hasAuthority('events:*')")
    @Operation(summary = "Create an event", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Event created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Campus not found")
    })
    public ResponseEntity<Void> createEvent(@PathVariable String campusCode) {
        return ResponseEntity.status(501).build();
    }

    @PostMapping("/{campusCode}/bulk")
    @PreAuthorize("hasAuthority('events:write') OR hasAuthority('events:*')")
    @Operation(summary = "Create multiple events", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Events created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Campus not found")
    })
    public ResponseEntity<Void> createEvents(@PathVariable String campusCode) {
        return ResponseEntity.status(501).build();
    }

    @PutMapping("/{campusCode}/{eventId}")
    @PreAuthorize("hasAuthority('events:write') OR hasAuthority('events:*')")
    @Operation(summary = "Update an event", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
            @Parameter(name = "eventId", description = "The event id", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Campus or Event not found")
    })
    public ResponseEntity<Void> updateEvent(@PathVariable String campusCode, @PathVariable String eventId) {
        return ResponseEntity.status(501).build();
    }

    @DeleteMapping("/{campusCode}/{eventId}")
    @PreAuthorize("hasAuthority('events:write') OR hasAuthority('events:*')")
    @Operation(summary = "Delete an event", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
            @Parameter(name = "eventId", description = "The event id", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Campus or Event not found")
    })
    public ResponseEntity<Void> deleteEvent(@PathVariable String campusCode, @PathVariable String eventId) {
        return ResponseEntity.status(501).build();
    }
}
