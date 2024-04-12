package br.com.mendes.passin.repositories;

import br.com.mendes.passin.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository  extends JpaRepository<Event, String> {
}
