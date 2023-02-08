package by.belyahovich.dance_events.service.eventtype;

import by.belyahovich.dance_events.domain.EventType;

import java.util.Optional;

public interface EventTypeService {

    EventType createEventType (EventType eventType);

    Optional<EventType> findEventTypeByTitle (String type);

    void deleteEventType (EventType eventType);
}
