package br.com.mendes.passin.repositories;

import br.com.mendes.passin.domain.checkin.CheckIn;
import br.com.mendes.passin.services.CheckInService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckInRepository extends JpaRepository<CheckIn, String> {
    Optional<CheckIn> findByAttendeeId(String attendeeId);
}
