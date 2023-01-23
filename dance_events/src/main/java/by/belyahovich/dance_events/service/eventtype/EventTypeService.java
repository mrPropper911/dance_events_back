package by.belyahovich.dance_events.service.eventtype;

import by.belyahovich.dance_events.domain.EventType;

public interface EventTypeService {

    EventType createEventType (EventType eventType);

    void deleteEventType (EventType eventType);
}
