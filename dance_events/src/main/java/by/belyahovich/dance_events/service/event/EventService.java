package by.belyahovich.dance_events.service.event;

import by.belyahovich.dance_events.domain.Event;
import by.belyahovich.dance_events.dto.EventDTO;

import java.util.List;
import java.util.Optional;

public interface EventService {

    Optional<Event> findEventByTitle (String title);

    List<EventDTO> findEventLikeTitle (String likeTitle);

    List<EventDTO> findEventByEventType (String eventType);

    void deleteEventByTitle (String title);

    void createNewEvent (EventDTO eventDTO);

    void updateEvent (EventDTO eventDTO);

    List<EventDTO> findAllEventsSortedByStartDate ();
}
