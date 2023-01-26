package by.belyahovich.dance_events.service.event;

import by.belyahovich.dance_events.domain.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {

    Optional<Event> findEventByTitle (String title);

    void deleteEventByTitle (String title);

    Event createEvent (Event event);

    List<Event> findAllEvents ();
}
