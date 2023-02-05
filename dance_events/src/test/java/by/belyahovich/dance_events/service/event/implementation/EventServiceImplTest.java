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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("EventService unit-testing")
class EventServiceImplTest {

    private static final String NAME_OF_EVENT_1 = "SOME_NAME";

    protected Event event_1 = new Event();
    protected Event event_2 = new Event();

    private EventRepository eventRepository;
    private EventRepositoryJpa eventRepositoryJpa;
    private EventService eventService;
    private EventDTOMapper eventDTOMapper;
    private EventTypeService eventTypeService;

    @BeforeEach
    public void init() {
        eventRepositoryJpa = Mockito.mock(EventRepositoryJpa.class);
        eventRepository = Mockito.mock(EventRepository.class);
        eventDTOMapper = Mockito.mock(EventDTOMapper.class);
        eventTypeService = Mockito.mock(EventTypeService.class);
        eventService = new EventServiceImpl(eventRepository, eventDTOMapper, eventRepositoryJpa, eventTypeService);
        EventType eventType = new EventType();
        eventType.setId(1L);
        eventType.setType("SOME_TYPE");
        event_1.setTitle(NAME_OF_EVENT_1);
        event_2.setTitle("SOME_NAME_2");
        event_1.setEventType(eventType);
        event_2.setEventType(eventType);
    }

    @Test
    void findEventByTitle_withExistingEvent_shouldProperlyFindEvent() {
        //when
        when(eventRepositoryJpa.findEventByTitle(anyString())).thenReturn(Optional.of(event_1));
        Optional<Event> actualEvent = eventService.findEventByTitle(NAME_OF_EVENT_1);
        //then
        assertThat(actualEvent).isPresent();
        assertThat(actualEvent).isEqualTo(Optional.of(event_1));
    }

    @Test
    void findEventByTitle_withNotExistingEvent_shouldThrowException() {
        //when
        when(eventRepositoryJpa.findEventByTitle(anyString())).thenReturn(Optional.empty());
        //then
        assertThrows(ResourceNotFoundException.class,
                () -> eventService.findEventByTitle(NAME_OF_EVENT_1));
    }

    @Test
    void deleteEventByTitle_withExistingEvent_shouldProperlyReturnEvent() {
        //when
        when(eventRepositoryJpa.findEventByTitle(anyString())).thenReturn(Optional.of(event_1));
        //then
        eventService.deleteEventByTitle(NAME_OF_EVENT_1);
        verify(eventRepositoryJpa, times(1)).deleteEventByTitle(NAME_OF_EVENT_1);
    }

    @Test
    void deleteEventByTitle_withNotExistingEvent_shouldThrowException() {
        //when
        when(eventRepositoryJpa.findEventByTitle(anyString())).thenReturn(Optional.empty());
        //then
        assertThrows(ResourceNotFoundException.class,
                () -> eventService.deleteEventByTitle(NAME_OF_EVENT_1));
    }

//    @Test
//    void createEvent_withNotExistingEvent_shouldProperlyCreateNewEvent() {
//        //when
//        when(eventRepositoryJpa.findEventByTitle(anyString())).thenReturn(Optional.empty());
//        when(eventRepository.save(any(Event.class))).thenReturn(event_1);
//
//        //then
//        Event actualEvent = eventService.createEvent(event_1);
//        assertThat(actualEvent).isNotNull();
//        assertThat(actualEvent).isEqualTo(event_1);
//        verify(eventRepository, times(1)).save(event_1);
//    }

//    @Test
//    void createEvent_withExistingEvent_shouldThrowException() {
//        //when
//        when(eventRepositoryJpa.findEventByTitle(anyString())).thenReturn(Optional.of(event_1));
//        //then
//        assertThrows(ResourceNotFoundException.class,
//                () -> eventService.createEvent(event_1));
//    }

//    @Test
//    void findAllByOrderByStartDateAsc_withExistingEvent_shouldProperlyFindAllEvents() {
//        //when
//        List<EventDTO> expectedEventList = new ArrayList<>(Arrays.asList(event_1, event_2));
//        when(eventRepositoryJpa.findAllByOrderByStartDateAsc()).thenReturn(expectedEventList);
//        //then
//        List<EventDTO> actualAllEvents = eventService.findAllEvents();
//        assertThat(actualAllEvents).hasSize(expectedEventList.size());
//        assertThat(actualAllEvents.get(0).title()).isEqualTo(NAME_OF_EVENT_1);
//    }

}