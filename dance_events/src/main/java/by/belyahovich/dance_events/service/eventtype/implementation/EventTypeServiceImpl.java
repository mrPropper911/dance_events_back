package by.belyahovich.dance_events.service.eventtype.implementation;

import by.belyahovich.dance_events.config.ResourceNotFoundException;
import by.belyahovich.dance_events.domain.EventType;
import by.belyahovich.dance_events.repository.eventtype.EventTypeRepository;
import by.belyahovich.dance_events.service.eventtype.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventTypeServiceImpl implements EventTypeService {

    private final EventTypeRepository eventTypeRepository;

    @Autowired
    public EventTypeServiceImpl(EventTypeRepository eventTypeRepository) {
        this.eventTypeRepository = eventTypeRepository;
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
    public void deleteEventType(EventType eventType) {
        Optional<EventType> actualEventType = eventTypeRepository.findById(eventType.getId());
        if (actualEventType.isEmpty()){
            throw new ResourceNotFoundException("THIS EVENT TYPE WITH TITLE: " + eventType.getId() + " NOT EXISTS");
        }
        eventTypeRepository.delete(eventType);
    }
}
