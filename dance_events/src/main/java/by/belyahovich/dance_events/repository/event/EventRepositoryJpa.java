package by.belyahovich.dance_events.repository.event;

import by.belyahovich.dance_events.domain.Event;
import by.belyahovich.dance_events.domain.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepositoryJpa extends JpaRepository<Event, Long> {

    //Find event by title
    Optional<Event> findEventByTitle(String title);

    //Find events like title
    List<Event> findEventsByTitleContainingIgnoreCase(String likeTitle);

    List<Event> findEventsByEventType (EventType eventType);

    //Delete event by title
    void deleteEventByTitle(String title);

    //Find all events sorted by StartDate
    List<Event> findAllByOrderByStartDateAsc();
}
