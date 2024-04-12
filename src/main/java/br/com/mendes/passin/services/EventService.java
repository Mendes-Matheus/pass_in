package br.com.mendes.passin.services;

import br.com.mendes.passin.domain.attendee.Attendee;
import br.com.mendes.passin.domain.event.Event;
import br.com.mendes.passin.domain.event.exceptions.EventFullException;
import br.com.mendes.passin.domain.event.exceptions.EventNotFoundException;
import br.com.mendes.passin.dto.attendee.AttendeeIdDTO;
import br.com.mendes.passin.dto.attendee.AttendeeRequestDTO;
import br.com.mendes.passin.dto.event.EventIdDTO;
import br.com.mendes.passin.dto.event.EventRequestDTO;
import br.com.mendes.passin.dto.event.EventResponseDTO;
import br.com.mendes.passin.repositories.AttendeeRepository;
import br.com.mendes.passin.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;
    private final AttendeeRepository attendeeRepository;


    private Event getEventById(String eventId){
        return this.eventRepository.findById(eventId).orElseThrow(()
                -> new EventNotFoundException("Event not found with ID: " + eventId));
    }

    public EventResponseDTO getEventDetail(String eventId){
        Event event = this.getEventById(eventId);
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);
        return new EventResponseDTO(event, attendeeList.size());
    }

    public EventIdDTO createEvent(EventRequestDTO eventDTO){
        Event newEvent = new Event();

        newEvent.setTitle(eventDTO.title());
        newEvent.setDetails(eventDTO.details());
        newEvent.setMaximumAttendees(eventDTO.maximumAttendees());
        newEvent.setSlug(this.createSlug(eventDTO.title()));

        this.eventRepository.save(newEvent);

        return new EventIdDTO(newEvent.getId());
    }

    private String createSlug(String text){
        // esse método faz a decomposição canônica da string recebida por parâmetro
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("[^\\w\\s]", "")
                .replaceAll("\\s+", "-")
                .toLowerCase();
    }

    public AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeeRequestDTO attendeeRequestDTO){
        this.attendeeService.verifyAttendeeSubscription(eventId, attendeeRequestDTO.email());

        Event event = this.getEventById(eventId);
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);

        if(event.getMaximumAttendees() <= attendeeList.size()) throw new EventFullException("Event is full");

        Attendee newAttendee = new Attendee();
        newAttendee.setName(attendeeRequestDTO.name());
        newAttendee.setEmail(attendeeRequestDTO.email());
        newAttendee.setEvent(event);
        newAttendee.setCreatedAt(LocalDateTime.now());
        this.attendeeService.registerAttendee(newAttendee);

        return new AttendeeIdDTO(newAttendee.getId());
    }
}
