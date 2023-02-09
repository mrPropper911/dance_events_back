package by.belyahovich.dance_events.repository.event;

import by.belyahovich.dance_events.domain.Event;
import by.belyahovich.dance_events.domain.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepositoryJpa extends JpaRepository<Event, Long>, PagingAndSortingRepository<Event, Long> {

    /**
     * <h2>Find event by title</h2>
     *
     * @param title {@link Event} title
     * @return {@link Event}
     */
    Optional<Event> findEventByTitle(String title);

    /**
     * <h2>Search in events like title</h2>
     *
     * @param likeTitle searching param
     * @return list of {@link Event}
     */
    List<Event> findEventsByTitleContainingIgnoreCase(String likeTitle);

    /**
     * <h2>Find events by event type</h2>
     *
     * @param eventType title of event type
     * @return list of {@link Event}
     */
    List<Event> findEventsByEventType (EventType eventType);

    /**
     * <h2>Find all events sorted by StartDate</h2>
     *
     * @return list of {@link Event}
     */
    Page<Event> findAllByOrderByStartDateAsc(Pageable pageable);

    /**
     * <h2>Delete event by title</h2>
     *
     * @param title {@link Event} title
     */
    void deleteEventByTitle(String title);
}
