package by.belyahovich.dance_events.service.event.implementation;

import by.belyahovich.dance_events.config.ResourceNotFoundException;
import by.belyahovich.dance_events.domain.Event;
import by.belyahovich.dance_events.domain.EventType;
import by.belyahovich.dance_events.dto.EventDTO;
import by.belyahovich.dance_events.dto.EventDTOMapper;
import by.belyahovich.dance_events.repository.event.EventRepository;
import by.belyahovich.dance_events.repository.event.EventRepositoryJpa;
import by.belyahovich.dance_events.service.event.EventService;
import by.belyahovich.dance_events.service.eventtype.EventTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventDTOMapper eventDTOMapper;
    private final EventRepositoryJpa eventRepositoryJpa;

    private final EventTypeService eventTypeService;

    public EventServiceImpl(EventRepository eventRepository, EventDTOMapper eventDTOMapper,
                            EventRepositoryJpa eventRepositoryJpa, EventTypeService eventTypeService) {
        this.eventRepository = eventRepository;
        this.eventDTOMapper = eventDTOMapper;
        this.eventRepositoryJpa = eventRepositoryJpa;
        this.eventTypeService = eventTypeService;
    }

    @Override
    public Optional<Event> findEventByTitle(String title) {
        return Optional.ofNullable(eventRepositoryJpa.findEventByTitle(title).orElseThrow(
                () -> new ResourceNotFoundException("EVENT WITH TITLE: " + title + " NOT EXIST")
        ));
    }

    @Override
    public List<EventDTO> findEventLikeTitle(String likeTitle) {
        return eventRepositoryJpa.findEventsByTitleContainingIgnoreCase(likeTitle)
                .stream()
                .map(eventDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> findEventByEventType(String eventTypeTitle) {
        //searching event type for add to new event
        EventType eventTypeByTitle =
                eventTypeService.findEventTypeByTitle(eventTypeTitle).
                        orElseThrow(() -> new ResourceNotFoundException(
                                "THIS EVENT TYPE: " + eventTypeTitle + " NOT EXIST"
                        ));
        return eventRepositoryJpa.findEventsByEventType(eventTypeByTitle)
                .stream()
                .map(eventDTOMapper)
                .collect(Collectors.toList());
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
    public EventDTO createNewEvent(EventDTO eventDTO) {
        Optional<Event> eventByTitle = eventRepositoryJpa.findEventByTitle(eventDTO.title());
        if (eventByTitle.isPresent()) {
            throw new ResourceNotFoundException("THIS EVEN WITH TITLE: " + eventDTO.title() + " ALREADY EXISTS");
        }
        //searching event type for add to new event
        EventType eventTypeByTitle =
                eventTypeService.findEventTypeByTitle(eventDTO.eventTypeTitle()).
                        orElseThrow(() -> new ResourceNotFoundException(
                                "THIS EVENT TYPE: " + eventDTO.eventTypeTitle() + " NOT EXIST"
                        ));

        Event eventToSave = eventDTOMapper.toEvent(eventDTO);
        eventToSave.setActive(true);
        eventToSave.setEventType(eventTypeByTitle);

        Event saveEvent = eventRepository.save(eventToSave);
        return new EventDTOMapper().apply(saveEvent);
    }

    @Override
    public void updateEvent(EventDTO eventDTO) {
        //searching event type for update event
        EventType eventTypeByTitle =
                eventTypeService.findEventTypeByTitle(eventDTO.eventTypeTitle()).
                        orElseThrow(() -> new ResourceNotFoundException(
                                "THIS EVENT TYPE: " + eventDTO.eventTypeTitle() + " NOT EXIST"
                        ));
        Event eventByTitle = eventRepository.findById(eventDTO.id())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "THIS EVENT WITH ID : " + eventDTO.id() + " NOT EXIST"
                ));

        eventByTitle.setTitle(eventDTO.title());
        eventByTitle.setDescription(eventDTO.description());
        eventByTitle.setStartDate(eventDTO.startDate());
        eventByTitle.setEndDate(eventDTO.endDate());
        eventByTitle.setEventType(eventTypeByTitle);
        eventByTitle.setActive(eventDTO.active());

        eventRepository.save(eventByTitle);
    }

    @Override
    public List<EventDTO> findAllEvents() {
        return eventRepositoryJpa.findAllByOrderByStartDateAsc()
                .stream()
                .map(eventDTOMapper)
                .collect(Collectors.toList());
    }
}
