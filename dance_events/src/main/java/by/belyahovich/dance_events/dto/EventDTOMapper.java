package by.belyahovich.dance_events.dto;

import by.belyahovich.dance_events.domain.Event;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class EventDTOMapper implements Function<Event, EventDTO> {

    @Override
    public EventDTO apply(Event event) {
        return new EventDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getStartDate(),
                event.getEndDate(),
                event.getEventType().getType(),
                event.isActive()
        );
    }

    public Event toEvent(EventDTO eventDTO) {
        return new Event(eventDTO.title(), eventDTO.startDate(), eventDTO.endDate(),
                eventDTO.description());
    }
}
