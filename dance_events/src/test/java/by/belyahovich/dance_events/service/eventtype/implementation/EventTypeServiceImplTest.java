package by.belyahovich.dance_events.service.eventtype.implementation;

import by.belyahovich.dance_events.config.ResourceNotFoundException;
import by.belyahovich.dance_events.domain.EventType;
import by.belyahovich.dance_events.repository.eventtype.EventTypeRepository;
import by.belyahovich.dance_events.repository.eventtype.EventTypeRepositoryJpa;
import by.belyahovich.dance_events.service.eventtype.EventTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("EventTypeService unit-test")
class EventTypeServiceImplTest {

    private EventTypeService eventTypeService;

    private EventTypeRepository eventTypeRepository;
    private EventTypeRepositoryJpa eventTypeRepositoryJpa;

    @BeforeEach
    public void init() {
        eventTypeRepository = Mockito.mock(EventTypeRepository.class);
        eventTypeRepositoryJpa = Mockito.mock(EventTypeRepositoryJpa.class);
        eventTypeService = new EventTypeServiceImpl(eventTypeRepository, eventTypeRepositoryJpa);
    }

    @Test
    void createEventType_withNotExistingEvent_shouldProperlyCreateNewEventType() {
        //given
        EventType eventType = new EventType(1L, "SOME_TITLE");
        //when
        when(eventTypeRepository.findById(anyLong())).thenReturn(Optional.empty());
        eventTypeService.createEventType(eventType);
        //then
        ArgumentCaptor<EventType> argumentCaptor =
                ArgumentCaptor.forClass(EventType.class);
        verify(eventTypeRepository).save(argumentCaptor.capture());
        EventType capturedEventType = argumentCaptor.getValue();
        assertThat(capturedEventType).isEqualTo(eventType);
    }

    @Test
    void createEventType_withExistingEvent_shouldProperlyCreateNewEventType() {
        //given
        EventType eventType = new EventType(1L, "SOME_TITLE");
        //when
        when(eventTypeRepository.findById(anyLong())).thenReturn(Optional.of(eventType));
        //then
        assertThatThrownBy(() -> eventTypeService.createEventType(eventType))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("THIS EVENT TYPE WITH TITLE: 1 ALREADY EXISTS");
    }

    @Test
    public void findEventTypeByTitle_withExistingEventType_shouldProperlyFind() {
        //given
        EventType eventType = new EventType(1L, "SOME_TITLE");
        //when
        when(eventTypeRepositoryJpa.findEventTypeByType(anyString())).thenReturn(Optional.of(eventType));
        Optional<EventType> actual = eventTypeService.findEventTypeByTitle("SOME_TITLE");
        //then
        assertThat(actual).isPresent();
        assertThat(actual.get().getType()).isEqualTo("SOME_TITLE");
    }

    @Test
    public void findEventTypeByTitle_withNotExistingEventType_shouldProperlyFind() {
        //when
        when(eventTypeRepositoryJpa.findEventTypeByType(anyString())).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> eventTypeService.findEventTypeByTitle("SOME_TITLE"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("THIS EVENT TYPE WITH TITLE: SOME_TITLE NOT EXISTS");
    }

    @Test
    void deleteEventType_withExistingEvent_shouldProperlyDeleteEventType() {
        //given
        EventType eventType = new EventType(1L, "SOME_TITLE");
        //when
        when(eventTypeRepository.findById(anyLong())).thenReturn(Optional.of(eventType));
        eventTypeService.deleteEventType(eventType);
        //then
        ArgumentCaptor<EventType> argumentCaptor =
                ArgumentCaptor.forClass(EventType.class);
        verify(eventTypeRepository).delete(argumentCaptor.capture());
        EventType capturedEventType = argumentCaptor.getValue();
        assertThat(capturedEventType).isEqualTo(eventType);
    }

    @Test
    void deleteEventType_withNotExistingEvent_shouldProperlyDeleteEventType() {
        //given
        EventType eventType = new EventType(1L, "SOME_TITLE");
        //when
        when(eventTypeRepository.findById(anyLong())).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> eventTypeService.deleteEventType(eventType))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("THIS EVENT TYPE WITH TITLE: 1 NOT EXISTS");
    }
}