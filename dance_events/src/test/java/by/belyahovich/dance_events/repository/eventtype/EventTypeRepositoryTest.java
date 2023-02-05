package by.belyahovich.dance_events.repository.eventtype;

import by.belyahovich.dance_events.domain.EventType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EventTypeRepository module test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventTypeRepositoryTest {

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Autowired
    private EventTypeRepositoryJpa eventTypeRepositoryJpa;

    @Sql(scripts = {"/sql/clearDatabase.sql", "/sql/addRolesForUsers.sql"})
    @Test
    public void findEventTypeByType_withExistingEventType_shouldProperlyFindEventType() {
        //given
        String EXISTING_EVENT_TYPE = "Regional";
        //when
        Optional<EventType> ACTUAL_EVENT_TYPE = eventTypeRepositoryJpa.findEventTypeByType(EXISTING_EVENT_TYPE);
        //then
        assertThat(ACTUAL_EVENT_TYPE).isPresent();
    }

    @Sql(scripts = {"/sql/clearDatabase.sql", "/sql/addRolesForUsers.sql"})
    @Test
    public void findEventTypeByType_withNotExistingEventType_shouldProperlyFindEventType() {
        //given
        String NOT_EXISTING_EVENT_TYPE = "NOT_EXISTING";
        //when
        Optional<EventType> ACTUAL_EVENT_TYPE = eventTypeRepositoryJpa.findEventTypeByType(NOT_EXISTING_EVENT_TYPE);
        //then
        assertThat(ACTUAL_EVENT_TYPE).isNotPresent();
    }

    @Sql(scripts = {"/sql/clearDatabase.sql", "/sql/addRolesForUsers.sql"})
    @Test
    public void findAll_with4Entity_shouldProperlyFindAllEventType() {
        //given
        int EXPECTED_COUNT_OF_ENTITY_TYPE_IN_DB = 4;
        //when
        Iterable<EventType> actualTypeOfEvent = eventTypeRepository.findAll();
        //then
        assertThat(actualTypeOfEvent).hasSize(EXPECTED_COUNT_OF_ENTITY_TYPE_IN_DB);
    }

    @Sql(scripts = {"/sql/clearDatabase.sql"})
    @Test
    public void save_withNewEventType_shouldProperlySaveNewEventType() {
        //given
        EventType newEventType = new EventType();
        newEventType.setType("Localize");
        EventType expectedEventTypeFromDB = eventTypeRepository.save(newEventType);
        assertThat(expectedEventTypeFromDB).isNotNull();
        //when
        Optional<EventType> actualEventTypeFromDB = eventTypeRepository.findById(expectedEventTypeFromDB.getId());
        //then
        assertThat(actualEventTypeFromDB.orElseThrow()).isEqualTo(expectedEventTypeFromDB);
    }
}