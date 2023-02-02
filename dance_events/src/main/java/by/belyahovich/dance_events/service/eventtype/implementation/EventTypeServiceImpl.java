package by.belyahovich.dance_events.service.eventtype.implementation;

import by.belyahovich.dance_events.config.ResourceNotFoundException;
import by.belyahovich.dance_events.domain.EventType;
import by.belyahovich.dance_events.repository.eventtype.EventTypeRepository;
import by.belyahovich.dance_events.repository.eventtype.EventTypeRepositoryJpa;
import by.belyahovich.dance_events.service.eventtype.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventTypeServiceImpl implements EventTypeService {

    private final EventTypeRepository eventTypeRepository;
    private final EventTypeRepositoryJpa eventTypeRepositoryJpa;

    @Autowired
    public EventTypeServiceImpl(EventTypeRepository eventTypeRepository,
                                EventTypeRepositoryJpa eventTypeRepositoryJpa) {
        this.eventTypeRepository = eventTypeRepository;
        this.eventTypeRepositoryJpa = eventTypeRepositoryJpa;
    }

    @Override
    public EventType createEventType(EventType eventType) {
        Optional<EventType> actualEventType = eventTypeRepository.findById(eventType.getId());
        if (actualEventType.isPresent()){
            throw new ResourceNotFoundException("THIS EVENT TYPE WITH TITLE: " + eventType.getId() + " ALREADY EXISTS");
        }
        return eventTypeRepository.save(eventType);
    }

    @Override
    public Optional<EventType> findEventTypeByTitle(String type) {
        return Optional.of(eventTypeRepositoryJpa.findEventTypeByType(type)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "THIS EVENT TYPE WITH TITLE: " + type + " NOT EXISTS"
                ))
        );
    }

    @Override
    public void deleteEventType(EventType eventType) {
        Optional<EventType> actualEventType = eventTypeRepository.findById(eventType.getId());
        if (actualEventType.isEmpty()){
            throw new ResourceNotFoundException("THIS EVENT TYPE WITH TITLE: " + eventType.getId() + " NOT EXISTS");
        }
        eventTypeRepository.delete(eventType);
    }
}
