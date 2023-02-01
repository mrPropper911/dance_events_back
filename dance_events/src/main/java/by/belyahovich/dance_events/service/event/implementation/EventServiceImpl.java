package by.belyahovich.dance_events.service.event.implementation;

import by.belyahovich.dance_events.config.ResourceNotFoundException;
import by.belyahovich.dance_events.domain.Event;
import by.belyahovich.dance_events.domain.EventType;
import by.belyahovich.dance_events.repository.event.EventRepository;
import by.belyahovich.dance_events.repository.event.EventRepositoryJpa;
import by.belyahovich.dance_events.service.event.EventService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventRepositoryJpa eventRepositoryJpa;

    public EventServiceImpl(EventRepository eventRepository, EventRepositoryJpa eventRepositoryJpa) {
        this.eventRepository = eventRepository;
        this.eventRepositoryJpa = eventRepositoryJpa;
    }

    @Override
    public Optional<Event> findEventByTitle(String title) {
        return Optional.ofNullable(eventRepositoryJpa.findEventByTitle(title).orElseThrow(
                () -> new ResourceNotFoundException("EVENT WITH TITLE: " + title + " NOT EXIST")
        ));
    }

    @Override
    public void deleteEventByTitle(String title) {
        Optional<Event> eventByTitle = eventRepositoryJpa.findEventByTitle(title);
        if (eventByTitle.isEmpty()) {
            throw new ResourceNotFoundException("THIS EVENT WITH TITLE: " + title + " NOT EXISTS");
        }
        eventRepositoryJpa.deleteEventByTitle(title);
    }

    @Override
    public Event createEvent(Event event) {
        Optional<Event> eventByTitle = eventRepositoryJpa.findEventByTitle(event.getTitle());
        if (eventByTitle.isPresent()) {
            throw new ResourceNotFoundException("THIS EVEN WITH TITLE: " + event.getTitle() + " ALREADY EXISTS");
        }
        return eventRepository.save(event);
    }

    @Override
    public List<Event> findAllEvents() {
        List<Event> returnAllEvents = new ArrayList<>();
        Iterable<Event> allEvents = eventRepositoryJpa.findAllByOrderByStartDateAsc();
        for (Event iter : allEvents) {
            returnAllEvents.add(new Event(iter.getTitle(), iter.getStartDate(), iter.getEndDate(),
                    iter.getDescription(), iter.isActive(), new EventType(iter.getEventType().getType())));
        }
        return returnAllEvents;
    }
}
