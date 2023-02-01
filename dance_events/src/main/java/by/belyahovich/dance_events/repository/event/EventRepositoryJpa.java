package by.belyahovich.dance_events.repository.event;

import by.belyahovich.dance_events.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepositoryJpa extends JpaRepository<Event, Long> {

    //Find event by title
    Optional<Event> findEventByTitle(String title);

    //Delete event by title
    void deleteEventByTitle(String title);

    //Find all events sorted by StartDate
    Iterable<Event> findAllByOrderByStartDateAsc();
}
