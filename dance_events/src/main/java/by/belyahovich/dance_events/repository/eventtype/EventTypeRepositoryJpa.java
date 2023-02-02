package by.belyahovich.dance_events.repository.eventtype;

import by.belyahovich.dance_events.domain.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventTypeRepositoryJpa extends JpaRepository<EventType, Long> {

    Optional<EventType> findEventTypeByType(String type);
}
