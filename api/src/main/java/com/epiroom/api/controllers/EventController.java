package com.epiroom.api.controllers;

import com.epiroom.api.model.Event;
import com.epiroom.api.model.dto.event.MyEvent;
import com.epiroom.api.repository.EventRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.time.DayOfWeek;

@Tag(name = "Event", description = "Events API")
@RestController
@RequestMapping("/events")
public class EventController {
    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get all events for the current user", parameters = {
            @Parameter(name = "start", description = "The start date"),
            @Parameter(name = "end", description = "The end date")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Events found", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MyEvent.class)))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid page or size"),
            @ApiResponse(responseCode = "403", description = "Not authenticated")
    })
    public ResponseEntity<List<MyEvent>> getMyEvents(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date start, @RequestParam(required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (start == null) {
            LocalDate now = LocalDate.now();
            LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            start = Date.from(startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        if (end == null) {
            LocalDate now = LocalDate.now();
            LocalDate endOfNextWeek = now.plusWeeks(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            end = Date.from(endOfNextWeek.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
        }

        List<Event> events = eventRepository.findAllByStartAfterAndEndBeforeAndUsers_Mail(start, end, authentication.getName());
        return ResponseEntity.ok(events.stream().map(MyEvent::new).toList());
    }
}

