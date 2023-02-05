package by.belyahovich.dance_events.repository.eventtype;

import by.belyahovich.dance_events.domain.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventTypeRepositoryJpa extends JpaRepository<EventType, Long> {

    /**
     * <h2>Find event type by type</h2>
     *
     * @param type title of event type
     * @return {@link EventType}
     */
    Optional<EventType> findEventTypeByType(String type);
}
