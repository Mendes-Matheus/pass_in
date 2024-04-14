package br.com.mendes.passin.services;


import br.com.mendes.passin.domain.attendee.Attendee;
import br.com.mendes.passin.domain.checkin.CheckIn;
import br.com.mendes.passin.domain.checkin.CheckInAlreadyExistsException;
import br.com.mendes.passin.repositories.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckInService {
    private final CheckInRepository checkInRepository;

    public Optional<CheckIn> getCheckIn(String attendeeId){
        return this.checkInRepository.findByAttendeeId(attendeeId);
    }

    public void registerCheckIn(Attendee attendee){
        this.verifyCheckInExists(attendee.getId());

        CheckIn newCheckIn = new CheckIn();
        newCheckIn.setAttendee(attendee);
        newCheckIn.setCreatedAt(LocalDateTime.now());

        checkInRepository.save(newCheckIn);
    }

    public void verifyCheckInExists(String attendeeId){
        Optional<CheckIn> checkIn = this.getCheckIn(attendeeId);

        if(checkIn.isPresent()) throw new CheckInAlreadyExistsException("CheckIn already exists!");
    }
}
