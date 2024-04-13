package br.com.mendes.passin.services;

import br.com.mendes.passin.domain.attendee.Attendee;
import br.com.mendes.passin.domain.attendee.exceptions.AttendeeAlreadyRegisteredException;
import br.com.mendes.passin.domain.checkin.CheckIn;
import br.com.mendes.passin.dto.attendee.AttendeeDetailDTO;
import br.com.mendes.passin.dto.attendee.AttendeesListResponseDTO;
import br.com.mendes.passin.repositories.AttendeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final CheckInService checkInService;

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

    public AttendeesListResponseDTO getEventsAttendee(String eventId){
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

        List<AttendeeDetailDTO> attendeeDetailList = attendeeList.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkInService.getCheckIn(attendee.getId());
            // LocalDateTime checkedInAt = checkIn.isPresent() ? checkIn.get().getCreatedAt() : null;
            LocalDateTime checkedInAt = checkIn.<LocalDateTime>map(CheckIn::getCreatedAt).orElse(null);
            return new AttendeeDetailDTO(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkedInAt);
        }).toList();
        return new AttendeesListResponseDTO(attendeeDetailList);
    }
}
