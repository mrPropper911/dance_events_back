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
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("EventService unit-test")
class EventServiceImplTest {

    private EventService eventService;

    private EventRepository eventRepository;
    private EventRepositoryJpa eventRepositoryJpa;
    private EventDTOMapper eventDTOMapper;
    private EventTypeService eventTypeService;

    @BeforeEach
    public void init() {
        eventRepositoryJpa = Mockito.mock(EventRepositoryJpa.class);
        eventRepository = Mockito.mock(EventRepository.class);
        eventDTOMapper = Mockito.mock(EventDTOMapper.class);
        eventTypeService = Mockito.mock(EventTypeService.class);
        eventService = new EventServiceImpl(
                eventRepository,
                eventDTOMapper,
                eventRepositoryJpa,
                eventTypeService
        );
    }

    @Test
    void findEventByTitle_withExistingEvent_shouldProperlyFindEvent() {
        //given
        Event event_1 = new Event(
                1L,
                "Title",
                new Date(System.currentTimeMillis() + 10_000),
                new Date(System.currentTimeMillis() + 90_000),
                "Description"
        );
        //when
        when(eventRepositoryJpa.findEventByTitle(anyString())).thenReturn(Optional.of(event_1));
        Optional<Event> actualEvent = eventService.findEventByTitle("Title");
        //then
        assertThat(actualEvent).isPresent();
        assertThat(actualEvent).isEqualTo(Optional.of(event_1));
    }

    @Test
    void findEventByTitle_withNotExistingEvent_shouldThrowException() {
        //when
        when(eventRepositoryJpa.findEventByTitle(anyString())).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> eventService.findEventByTitle("Title"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("EVENT WITH TITLE: Title NOT EXIST");
    }

    @Test
    void findEventLikeTitle_withExistingEvent_shouldProperlyFindEvent() {
        //given
        Event event_1 = new Event(
                1L,
                "Title",
                new Date(System.currentTimeMillis() + 10_000),
                new Date(System.currentTimeMillis() + 90_000),
                "Description"
        );
        EventDTO eventDTO = new EventDTO(
                1L,
                "Title",
                "Description",
                new Date(System.currentTimeMillis() + 10_000),
                new Date(System.currentTimeMillis() + 90_000),
                "Ev_Type",
                true
        );
        //when
        when(eventRepositoryJpa.findEventsByTitleContainingIgnoreCase(anyString())).thenReturn(List.of(event_1));
        when(eventDTOMapper.apply(any())).thenReturn(eventDTO);
        List<EventDTO> actualEvent = eventService.findEventLikeTitle("Tit");
        //then
        assertThat(actualEvent).hasSize(1);
    }

    @Test
    public void findEventByEventType_withNotExistingType_shouldThrowException() {
        //when
        when(eventTypeService.findEventTypeByTitle(anyString())).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> eventService.findEventByEventType("EV_TYPE"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("THIS EVENT TYPE: EV_TYPE NOT EXIST");

    }

    @Test
    public void findEventByEventType_withExistingEventType_shouldProperlyFound() {
        //given
        EventType eventType = new EventType(1L, "EV_TYPE");
        EventDTO eventDTO = new EventDTO(
                1L,
                "Title",
                "Description",
                new Date(System.currentTimeMillis() + 10_000),
                new Date(System.currentTimeMillis() + 90_000),
                "EV_TYPE",
                true
        );
        Event event_1 = new Event(
                1L,
                "Title",
                new Date(System.currentTimeMillis() + 10_000),
                new Date(System.currentTimeMillis() + 90_000),
                "Description"
        );
        //when
        when(eventTypeService.findEventTypeByTitle(anyString())).thenReturn(Optional.of(eventType));
        when(eventRepositoryJpa.findEventsByEventType(eventType)).thenReturn(List.of(event_1));
        when(eventDTOMapper.apply(any())).thenReturn(eventDTO);
        List<EventDTO> actualEvType = eventService.findEventByEventType("EV_TYPE");
        //then
        assertThat(actualEvType).isNotEmpty();
        assertThat(actualEvType).hasSize(1);
    }

    @Test
    void deleteEventByTitle_withExistingEvent_shouldProperlyDeleteEvent() {
        //given
        Event event_1 = new Event(
                1L,
                "Title",
                new Date(System.currentTimeMillis() + 10_000),
                new Date(System.currentTimeMillis() + 90_000),
                "Description"
        );
        //when
        when(eventRepositoryJpa.findEventByTitle(anyString())).thenReturn(Optional.of(event_1));
        //then
        eventService.deleteEventByTitle("Title");
        verify(eventRepositoryJpa).deleteEventByTitle("Title");
    }

    @Test
    void deleteEventByTitle_withNotExistingEvent_shouldThrowException() {
        //when
        when(eventRepositoryJpa.findEventByTitle(anyString())).thenReturn(Optional.empty());
        //then
        assertThrows(ResourceNotFoundException.class,
                () -> eventService.deleteEventByTitle(anyString()));
    }

    @Test
    void createNewEvent_withExistingEvent_shouldThrowException() {
        //given
        Event event_1 = new Event(
                1L,
                "Title",
                new Date(System.currentTimeMillis() + 10_000),
                new Date(System.currentTimeMillis() + 90_000),
                "Description"
        );
        EventDTO eventDTO = new EventDTO(
                1L,
                "Title",
                "Description",
                new Date(System.currentTimeMillis() + 10_000),
                new Date(System.currentTimeMillis() + 90_000),
                "EV_TYPE",
                true
        );
        //when
        when(eventRepositoryJpa.findEventByTitle(anyString())).thenReturn(Optional.of(event_1));
        //then
        assertThatThrownBy(() -> eventService.createNewEvent(eventDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("THIS EVEN WITH TITLE: Title ALREADY EXISTS");
        verify(eventRepository, never()).save(any());
    }

    @Test
    void createNewEvent_withNotExistingEventType_shouldThrowException() {
        //given
        EventDTO eventDTO = new EventDTO(
                1L,
                "Title",
                "Description",
                new Date(System.currentTimeMillis() + 10_000),
                new Date(System.currentTimeMillis() + 90_000),
                "EV_TYPE",
                true
        );
        //when
        when(eventRepositoryJpa.findEventByTitle(anyString())).thenReturn(Optional.empty());
        when(eventTypeService.findEventTypeByTitle(anyString())).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> eventService.createNewEvent(eventDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("THIS EVENT TYPE: EV_TYPE NOT EXIST");

        verify(eventRepository, never()).save(any());
    }

    @Test
    void createNewEvent_withExistingEventType_shouldProperlyCreate() {
        //given
        EventType eventType = new EventType(1L, "EV_TYPE");
        Event event_1 = new Event(
                1L,
                "Title",
                new Date(System.currentTimeMillis() + 10_000),
                new Date(System.currentTimeMillis() + 90_000),
                "Description"
        );
        EventDTO eventDTO = new EventDTO(
                1L,
                "Title",
                "Description",
                new Date(System.currentTimeMillis() + 10_000),
                new Date(System.currentTimeMillis() + 90_000),
                "EV_TYPE",
                true
        );
        //when
        when(eventRepositoryJpa.findEventByTitle(anyString())).thenReturn(Optional.empty());
        when(eventTypeService.findEventTypeByTitle(anyString())).thenReturn(Optional.of(eventType));
        when(eventDTOMapper.toEvent(any())).thenReturn(event_1);
        eventService.createNewEvent(eventDTO);
        //then

        ArgumentCaptor<Event> userArgumentCaptor =
                ArgumentCaptor.forClass(Event.class);

        verify(eventRepository).save(userArgumentCaptor.capture());
        Event capturedEvent = userArgumentCaptor.getValue();
        assertThat(capturedEvent).isEqualTo(event_1);
    }

    @Test
    public void updateEvent_withNotExistingEventType_shouldThrowException() {
        //given
        EventDTO eventDTO = new EventDTO(
                1L,
                "Title",
                "Description",
                new Date(System.currentTimeMillis() + 10_000),
                new Date(System.currentTimeMillis() + 90_000),
                "EV_TYPE",
                true
        );
        //when
        when(eventTypeService.findEventTypeByTitle(anyString())).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> eventService.updateEvent(eventDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("THIS EVENT TYPE: EV_TYPE NOT EXIST");
        verify(eventRepository, never()).save(any());
    }

    @Test
    public void updateEvent_withNotExistingEvent_shouldThrowException() {
        //given
        EventType eventType = new EventType(1L, "EV_TYPE");
        EventDTO eventDTO = new EventDTO(
                1L,
                "Title",
                "Description",
                new Date(System.currentTimeMillis() + 10_000),
                new Date(System.currentTimeMillis() + 90_000),
                "EV_TYPE",
                true
        );
        //when
        when(eventTypeService.findEventTypeByTitle(anyString())).thenReturn(Optional.of(eventType));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> eventService.updateEvent(eventDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("THIS EVENT WITH ID: 1 NOT EXIST");
        verify(eventRepository, never()).save(any());
    }

    @Test
    public void updateEvent_withExistingEvent_shouldProperlyEvent() {
        //given
        EventType eventType = new EventType(1L, "EV_TYPE");
        EventDTO eventDTO = new EventDTO(
                1L,
                "Title",
                "Description",
                new Date(System.currentTimeMillis() + 10_000),
                new Date(System.currentTimeMillis() + 90_000),
                "EV_TYPE",
                true
        );
        Event event_1 = new Event(
                1L,
                "Title",
                new Date(System.currentTimeMillis() + 10_000),
                new Date(System.currentTimeMillis() + 90_000),
                "Description"
        );
        //when
        when(eventTypeService.findEventTypeByTitle(anyString())).thenReturn(Optional.of(eventType));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event_1));
        eventService.updateEvent(eventDTO);
        //then
        ArgumentCaptor<Event> eventArgumentCaptor =
                ArgumentCaptor.forClass(Event.class);

        verify(eventRepository).save(eventArgumentCaptor.capture());
        Event capturedEvent = eventArgumentCaptor.getValue();
        assertThat(capturedEvent).isEqualTo(event_1);
    }

    @Test
    public void findAllEventsSortedByStartDate_withExistingEvents_shouldProperlyFindAllEvents() {
        //given
        Event event_1 = new Event(
                1L,
                "Title",
                new Date(System.currentTimeMillis() + 10_000),
                new Date(System.currentTimeMillis() + 90_000),
                "Description"
        );
        EventDTO eventDTO = new EventDTO(
                1L,
                "Title",
                "Description",
                new Date(System.currentTimeMillis() + 10_000),
                new Date(System.currentTimeMillis() + 90_000),
                "EV_TYPE",
                true
        );
        List<Event> eventList = List.of(event_1);
        Page<Event> page = new PageImpl<>(eventList);
        //when
        when(eventRepositoryJpa.findAllByOrderByStartDateAsc(any())).thenReturn(page);
        when(eventDTOMapper.apply(any())).thenReturn(eventDTO);
        List<EventDTO> actualAllEventsSortedByStartDate = eventService.findAllEventsSortedByStartDate(0, 1);
        //then
        assertThat(actualAllEventsSortedByStartDate).isNotEmpty();
        assertThat(actualAllEventsSortedByStartDate).hasSize(1);
    }

    @Test
    public void findAllEventsSortedByStartDate_withNotExistingPage_shouldProperlyReturnEmptyList() {
        //given
        EventDTO eventDTO = new EventDTO(
                1L,
                "Title",
                "Description",
                new Date(System.currentTimeMillis() + 10_000),
                new Date(System.currentTimeMillis() + 90_000),
                "EV_TYPE",
                true
        );
        List<Event> eventList = new ArrayList<>();
        Page<Event> page = new PageImpl<>(eventList);
        //when
        when(eventRepositoryJpa.findAllByOrderByStartDateAsc(any())).thenReturn(page);
        when(eventDTOMapper.apply(any())).thenReturn(eventDTO);
        List<EventDTO> actualAllEventsSortedByStartDate = eventService.findAllEventsSortedByStartDate(52, 13);
        //then
        assertThat(actualAllEventsSortedByStartDate).isEmpty();
    }
}