package com.epiroom.api.controllers;

import com.epiroom.api.model.Activity;
import com.epiroom.api.model.Campus;
import com.epiroom.api.model.Event;
import com.epiroom.api.model.Room;
import com.epiroom.api.model.dto.event.ActivityEvent;
import com.epiroom.api.model.dto.event.EventInputDTO;
import com.epiroom.api.model.dto.event.FullEvent;
import com.epiroom.api.model.dto.event.PaginatedEvent;
import com.epiroom.api.repository.ActivityRepository;
import com.epiroom.api.repository.CampusRepository;
import com.epiroom.api.repository.EventRepository;
import com.epiroom.api.repository.RoomRepository;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@CrossOrigin(origins = "*")
@Tag(name = "Events", description = "Event API")
@RestController
@RequestMapping("/events")
public class EventController {
    private final ActivityRepository activityRepository;
    CampusRepository campusRepository;
    EventRepository eventRepository;
    RoomRepository roomRepository;

    public EventController(CampusRepository campusRepository, EventRepository eventRepository, RoomRepository roomRepository, ActivityRepository activityRepository) {
        this.campusRepository = campusRepository;
        this.eventRepository = eventRepository;
        this.roomRepository = roomRepository;
        this.activityRepository = activityRepository;
    }

    @GetMapping("/{campusCode}/today")
    @Operation(summary = "Get all events of a campus for today", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Activities found", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FullEvent.class))
                    )
            }),
            @ApiResponse(responseCode = "404", description = "Campus not found")
    })
    public ResponseEntity<List<FullEvent>> getEventsByCampusCodeToday(@PathVariable String campusCode) {
        Date start = Date.from(LocalDateTime.of(LocalDate.now(), LocalTime.MIN).toInstant(java.time.ZoneOffset.UTC));
        Date end = Date.from(LocalDateTime.of(LocalDate.now(), LocalTime.MAX).toInstant(java.time.ZoneOffset.UTC));
        Campus campus = campusRepository.findByCode(campusCode);
        if (campus == null)
            return ResponseEntity.badRequest().build();
        List<Event> events = eventRepository.findAllByStartGreaterThanEqualAndEndLessThanEqualAndCampusCode(start, end, campusCode);
        return ResponseEntity.ok(events.stream().map(FullEvent::new).toList());
    }

    @GetMapping("/{campusCode}")
    @Operation(summary = "Get all events of a campus (Paginated)", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
            @Parameter(name = "page", description = "The page number"),
            @Parameter(name = "entries", description = "The number of entries per page"),
            @Parameter(name = "startDate", description = "The start date (Timestamp)"),
            @Parameter(name = "endDate", description = "The end date (Timestamp)")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Events found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PaginatedEvent.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid page, entries or dates (page < 1, entries < 1 or entries > 100)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Campus not found", content = @Content)
    })
    public ResponseEntity<PaginatedEvent> getEventsByCampusCode(@PathVariable String campusCode, @RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "50") int entries, @RequestParam(required = false) Long startDate, @RequestParam(required = false) Long endDate) {
        if (page < 1 || entries < 1 || entries > 100 || startDate != null && endDate != null && startDate > endDate)
            return ResponseEntity.badRequest().build();
        Campus campus = campusRepository.findByCode(campusCode);
        if (campus == null)
            return ResponseEntity.notFound().build();
        List<Event> events;
        if (startDate != null && endDate != null)
            events = eventRepository.findAllByCampusCodeAndStartDateGreaterThanEqualAndEndDateLessThanEqual(PageRequest.of(page - 1, entries), campusCode, startDate, endDate);
        else
            events = eventRepository.findAllByCampusCode(PageRequest.of(page - 1, entries), campusCode);
        return ResponseEntity.ok(new PaginatedEvent(page, events.size(), events.stream().map(FullEvent::new).toList()));
    }

    @PostMapping("/{campusCode}")
    @PreAuthorize("hasAuthority('events:write') OR hasAuthority('events:*')")
    @Operation(summary = "Create an event", parameters = {
            @Parameter(name = "campusCode", description = "The campus code", required = true),
    }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The event to create", required = true, content = @Content(schema = @Schema(implementation = EventInputDTO.class))))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Event created", content = @Content(schema = @Schema(implementation = FullEvent.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Campus not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Event already exists", content = @Content(schema = @Schema(implementation = FullEvent.class)))
    })
    public ResponseEntity<FullEvent> createEvent(@PathVariable String campusCode, @RequestBody EventInputDTO eventInputDTO) {
        if (eventRepository.existsById(eventInputDTO.getId()))
            return ResponseEntity.status(409).body(new FullEvent(eventRepository.findByIdAndCampusCode(eventInputDTO.getId(),campusCode)));
        Campus campus = campusRepository.findByCode(campusCode);
        if (campus == null)
            return ResponseEntity.notFound().build();
        Room room = roomRepository.findById(eventInputDTO.getRoomId());
        if (room == null)
            return ResponseEntity.badRequest().build();
        Activity activity = activityRepository.findByIdAndCampusCode(eventInputDTO.getActivityId(), campusCode);
        if (activity == null)
            return ResponseEntity.badRequest().build();
        Event event = new Event(eventInputDTO, campus, activity, room);
        eventRepository.save(event);
        return ResponseEntity.status(201).body(new FullEvent(event));
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
