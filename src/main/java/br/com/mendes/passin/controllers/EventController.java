package br.com.mendes.passin.controllers;

import br.com.mendes.passin.dto.attendee.AttendeeIdDTO;
import br.com.mendes.passin.dto.attendee.AttendeeRequestDTO;
import br.com.mendes.passin.dto.attendee.AttendeesListResponseDTO;
import br.com.mendes.passin.dto.event.EventIdDTO;
import br.com.mendes.passin.dto.event.EventRequestDTO;
import br.com.mendes.passin.dto.event.EventResponseDTO;
import br.com.mendes.passin.services.AttendeeService;
import br.com.mendes.passin.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;
    private final AttendeeService attendeeService;

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String id){
        EventResponseDTO event = this.eventService.getEventDetail(id);
        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        EventIdDTO eventIdDTO = this.eventService.createEvent(body);

        var uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(eventIdDTO.eventId()).toUri();

        return ResponseEntity.created(uri).body(eventIdDTO);
    }

    @PostMapping("/{eventId}/attendees")
    public ResponseEntity<AttendeeIdDTO> registerParticipant(@PathVariable String eventId, @RequestBody AttendeeRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        AttendeeIdDTO attendeeIdDTO = this.eventService.registerAttendeeOnEvent(eventId, body);

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/badge").buildAndExpand(attendeeIdDTO.attendeeId()).toUri();

        return ResponseEntity.created(uri).body(attendeeIdDTO);
    }

    @GetMapping("/{eventId}/attendees")
    public ResponseEntity<AttendeesListResponseDTO> getEventAttendees(@PathVariable String eventId){
        AttendeesListResponseDTO attendeesListResponse = this.attendeeService.getEventsAttendee(eventId);
        return ResponseEntity.ok(attendeesListResponse);
    }



}
