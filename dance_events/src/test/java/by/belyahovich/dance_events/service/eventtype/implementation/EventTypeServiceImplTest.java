package by.belyahovich.dance_events.service.eventtype.implementation;

import by.belyahovich.dance_events.domain.EventType;
import by.belyahovich.dance_events.repository.eventtype.EventTypeRepository;
import by.belyahovich.dance_events.service.eventtype.EventTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("EventType service unit-test")
class EventTypeServiceImplTest {

    protected EventType eventType = new EventType();
    private EventTypeRepository eventTypeRepository;
    private EventTypeService eventTypeService;

    @BeforeEach
    public void init() {
        eventTypeRepository = Mockito.mock(EventTypeRepository.class);
        eventTypeService = new EventTypeServiceImpl(eventTypeRepository);

        eventType.setId(1L);
        eventType.setType("SOME_TITLE");
    }

    @Test
    void createEventType_withNotExistingEvent_shouldProperlyCreateNewEventType() {
        //when
        when(eventTypeRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(eventTypeRepository.save(any(EventType.class))).thenReturn(eventType);
        //then
        EventType actualEventType = eventTypeService.createEventType(eventType);
        assertThat(actualEventType).isNotNull();
        assertThat(actualEventType).isEqualTo(eventType);
        verify(eventTypeRepository, times(1)).save(eventType);
    }

    @Test
    void deleteEventType_withExistingEvent_shouldProperlyDeleteEventType() {
        //when
        when(eventTypeRepository.findById(anyLong())).thenReturn(Optional.of(eventType));
        //then
        eventTypeService.deleteEventType(eventType);
        verify(eventTypeRepository, times(1)).delete(eventType);
    }
}