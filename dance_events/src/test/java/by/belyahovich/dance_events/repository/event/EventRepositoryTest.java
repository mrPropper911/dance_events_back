package by.belyahovich.dance_events.repository.event;

import by.belyahovich.dance_events.domain.Event;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Event repository module test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventRepositoryJpa eventRepositoryJpa;

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
    public void findEventByTitle_withExistingEvent_shouldProperlyFindEventByTitle() {
        //given
        String EVENT_TITLE_FOR_SEARCH = "MINSK-666";
        //when
        Optional<Event> foundedEventByTitleFromDB = eventRepositoryJpa.findEventByTitle(EVENT_TITLE_FOR_SEARCH);
        //then
        assertThat(foundedEventByTitleFromDB).isPresent();
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