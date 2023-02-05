package by.belyahovich.dance_events.repository.event;

import by.belyahovich.dance_events.domain.Event;
import by.belyahovich.dance_events.domain.EventType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EventRepository module test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventRepositoryJpa eventRepositoryJpa;

    @Sql(scripts = {"/sql/clearDatabase.sql", "/sql/addRolesForUsers.sql"})
    @Test
    public void findEventByTitle_withExistingEvent_shouldProperlyFindEventByTitle() {
        //given
        String EVENT_TITLE_FOR_SEARCH_EXISTING = "MINSK-666";
        //when
        Optional<Event> ACTUAL_EVENT =
                eventRepositoryJpa.findEventByTitle(EVENT_TITLE_FOR_SEARCH_EXISTING);
        //then
        assertThat(ACTUAL_EVENT).isPresent();
        assertThat(ACTUAL_EVENT.get().getTitle()).isEqualTo(EVENT_TITLE_FOR_SEARCH_EXISTING);
    }

    @Sql(scripts = {"/sql/clearDatabase.sql", "/sql/addRolesForUsers.sql"})
    @Test
    public void findEventByTitle_withNotExistingEvent_shouldProperlyFindEventByTitle() {
        //given
        String EVENT_TITLE_FOR_SEARCH_EXISTING = "SOME_NOT_EXISTING_TITLE";
        //when
        Optional<Event> ACTUAL_EVENT =
                eventRepositoryJpa.findEventByTitle(EVENT_TITLE_FOR_SEARCH_EXISTING);
        //then
        assertThat(ACTUAL_EVENT).isEmpty();
    }

    @Sql(scripts = {"/sql/clearDatabase.sql", "/sql/addRolesForUsers.sql"})
    @Test
    public void findEventsByTitleContainingIgnoreCase_withExistingEvent_shouldProperlyFindEventByTitle() {
        //given
        String EVENT_TITLE_FOR_LIKE_SEARCH_EXISTING = "MINSK";
        String EVENT_TITLE_EXISTING = "MINSK-666";
        //when
        List<Event> ACTUAL_LIST_OF_EVENTS =
                eventRepositoryJpa.findEventsByTitleContainingIgnoreCase(EVENT_TITLE_FOR_LIKE_SEARCH_EXISTING);
        //then
        assertThat(ACTUAL_LIST_OF_EVENTS).isNotEmpty();
        assertThat(ACTUAL_LIST_OF_EVENTS.get(0).getTitle()).isEqualTo(EVENT_TITLE_EXISTING);
    }

    @Sql(scripts = {"/sql/clearDatabase.sql", "/sql/addRolesForUsers.sql"})
    @Test
    public void findEventsByTitleContainingIgnoreCase_withNotExistingEvent_shouldProperlyFindEventByTitle() {
        //given
        String EVENT_TITLE_FOR_LIKE_SEARCH_NOT_EXISTING = "NOT_EXISTING_TITLE";
        //when
        List<Event> ACTUAL_LIST_OF_EVENTS =
                eventRepositoryJpa.findEventsByTitleContainingIgnoreCase(EVENT_TITLE_FOR_LIKE_SEARCH_NOT_EXISTING);
        //then
        assertThat(ACTUAL_LIST_OF_EVENTS).isEmpty();
    }

    @Sql(scripts = {"/sql/clearDatabase.sql", "/sql/addRolesForUsers.sql"})
    @Test
    public void findEventsByEventType_withExistingEvent_shouldProperlyFindEventByEventType() {
        //given
        EventType EXISTING_EVENT_TYPE = new EventType(4L, "International");
        //when
        List<Event> ACTUAL_LIST_OF_EVENTS = eventRepositoryJpa.findEventsByEventType(EXISTING_EVENT_TYPE);
        //then
        assertThat(ACTUAL_LIST_OF_EVENTS).isNotEmpty();
        assertThat(ACTUAL_LIST_OF_EVENTS).hasSize(1);
    }

    @Sql(scripts = {"/sql/clearDatabase.sql", "/sql/addRolesForUsers.sql"})
    @Test
    public void findEventsByEventType_withNotExistingEvent_shouldProperlyFindEventByEventType() {
        //given
        EventType EXISTING_EVENT_TYPE = new EventType(35L, "NOT_EXISTING_TYPE");
        //when
        List<Event> ACTUAL_LIST_OF_EVENTS = eventRepositoryJpa.findEventsByEventType(EXISTING_EVENT_TYPE);
        //then
        assertThat(ACTUAL_LIST_OF_EVENTS).isEmpty();
    }

    @Sql(scripts = {"/sql/clearDatabase.sql", "/sql/addRolesForUsers.sql"})
    @Test
    public void findAllByOrderByStartDateAsc_withExistingEvent_shouldProperlyFindAllSortedEvent() {
        //given
        int EXPECTED_COUNT_OF_EVENTS_ON_DB = 4;
        //when
        List<Event> ACTUAL_EVENTS_FROM_DB = eventRepositoryJpa.findAllByOrderByStartDateAsc();
        //then
        assertThat(ACTUAL_EVENTS_FROM_DB).hasSize(EXPECTED_COUNT_OF_EVENTS_ON_DB);
        assertThat(ACTUAL_EVENTS_FROM_DB).isSortedAccordingTo(Comparator.comparing(Event::getStartDate));
    }

    @Sql(scripts = {"/sql/clearDatabase.sql", "/sql/addRolesForUsers.sql"})
    @Test
    public void findAll_with4Entity_shouldProperlyFindAllEvents() {
        //given
        int EXPECTED_COUNT_OF_EVENTS_ON_DB = 4;
        //when
        Iterable<Event> actualEventsFromDB = eventRepository.findAll();
        //then
        assertThat(actualEventsFromDB).hasSize(EXPECTED_COUNT_OF_EVENTS_ON_DB);
    }

    @Sql(scripts = {"/sql/clearDatabase.sql"})
    @Test
    public void save_withNewEvent_shouldProperlySaveNewEvent() {
        //given
        Event newEvent = createNewEvent();
        Event expectedEventInDb = eventRepository.save(newEvent);
        //when
        Optional<Event> actualEventFromDB = eventRepository.findById(expectedEventInDb.getId());
        //then
        assertThat(actualEventFromDB).isPresent();
        assertThat(actualEventFromDB.get()).isEqualTo(expectedEventInDb);
    }

    @Sql(scripts = {"/sql/clearDatabase.sql", "/sql/addRolesForUsers.sql"})
    @Test
    public void deleteByTitle_withExistingEvent_shouldProperlyDeleteEventFromDB() {
        //given
        String EVENT_TITLE_FOR_DELETE = "Loki-09";
        int ALL_COUNT_OFF_EVENTS_AFTER_DELETE_ONE = 4;
        int ALL_COUNT_OFF_EVENTS_BEFORE_DELETE_ONE = 5;
        Event newEven = createNewEvent();
        eventRepository.save(newEven);
        Iterable<Event> allEventsFromDB = eventRepository.findAll();
        assertThat(allEventsFromDB).hasSize(ALL_COUNT_OFF_EVENTS_BEFORE_DELETE_ONE);
        //when
        eventRepositoryJpa.deleteEventByTitle(EVENT_TITLE_FOR_DELETE);
        //then
        Iterable<Event> expectedEventsFromDB = eventRepository.findAll();
        assertThat(expectedEventsFromDB).hasSize(ALL_COUNT_OFF_EVENTS_AFTER_DELETE_ONE);
    }

    @Sql(scripts = {"/sql/clearDatabase.sql", "/sql/addRolesForUsers.sql"})
    @Test
    public void update_withExistingEvent_shouldProperlyUpdateEvent() {
        //given
        String EVEN_TITLE_FOR_UPDATE = "Baranovivhi-2023";
        Optional<Event> eventToUpdate = eventRepositoryJpa.findEventByTitle(EVEN_TITLE_FOR_UPDATE);
        assertThat(eventToUpdate).isPresent();
        //when
        eventToUpdate.get().setActive(false);
        eventRepository.save(eventToUpdate.get());
        Optional<Event> actualEvent = eventRepositoryJpa.findById(eventToUpdate.get().getId());
        //then
        assertThat(actualEvent).isPresent();
        assertThat(actualEvent.get().isActive()).isFalse();
    }

    private Event createNewEvent() {
        Event returnNewEvent = new Event();
        returnNewEvent.setTitle("Loki-09");
        returnNewEvent.setStartDate(new Date(2023 - 12 - 1));
        returnNewEvent.setEndDate(new Date(2023 - 12 - 4));
        returnNewEvent.setDescription("Big championship for children");
        returnNewEvent.setActive(true);
        return returnNewEvent;
    }
}