package br.com.mendes.passin.services;

import br.com.mendes.passin.domain.attendee.Attendee;
import br.com.mendes.passin.domain.attendee.exceptions.AttendeeAlreadyRegisteredException;
import br.com.mendes.passin.dto.attendee.AttendeeIdDTO;
import br.com.mendes.passin.repositories.AttendeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;

    public List<Attendee> getAllAttendeesFromEvent(String eventId){
        return this.attendeeRepository.findByEventId(eventId);
    }

    public void verifyAttendeeSubscription(String eventId, String email){
        Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);
        if(isAttendeeRegistered.isPresent()) throw new AttendeeAlreadyRegisteredException("Attendee is already registered!");
    }

    public Attendee registerAttendee(Attendee newAttendee){
        return this.attendeeRepository.save(newAttendee);
    }
}
