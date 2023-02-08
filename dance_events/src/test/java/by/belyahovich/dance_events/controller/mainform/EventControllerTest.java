package by.belyahovich.dance_events.controller.mainform;

import by.belyahovich.dance_events.dto.EventDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EventsController integration test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EventControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void getAllEventsSortedByStartDate_withExistingEvents_shouldProperlyFindAllEvents() {
        //when
        ResponseEntity<List<EventDTO>> response =
                testRestTemplate.exchange(
                        "/events",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<EventDTO> actualEvents = response.getBody();
        assertThat(actualEvents).hasSize(4);
        assertThat(actualEvents.get(2).title()).isEqualTo("MINSK-666");
    }

    @Test
    public void getEventsLikeTitle_withExistingEvents_shouldProperlyFindLikeTitle() {
        //given
        String url = "/events/title-search";
        String SEARCHING_TITLE_LIKE = "B";

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("title", SEARCHING_TITLE_LIKE);

        //when
        ResponseEntity<List<EventDTO>> response =
                testRestTemplate.exchange(
                        builder.toUriString(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<EventDTO> actualEvents = response.getBody();
        assertThat(actualEvents).hasSize(2);
        assertThat(actualEvents.get(0).title()).isEqualTo("Baranovivhi-2023");
    }

    @Test
    public void getEventsByEventsType_withExistingType_shouldProperlyFindEvent() {
        //given
        String url = "/events/type-search";
        String SEARCHING_EVENT_TYPE = "Urban";

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("eventType", SEARCHING_EVENT_TYPE);

        //when
        ResponseEntity<List<EventDTO>> response =
                testRestTemplate.exchange(
                        builder.toUriString(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<EventDTO> actualEvent = response.getBody();
        assertThat(actualEvent).hasSize(1);
        assertThat(actualEvent.get(0).title()).isEqualTo("Baranovivhi-2023");
    }

    @Test
    public void createNewEvent_withNotExistingEvent_shouldProperlySaveEvent() {
        //given
        EventDTO eventDTO = new EventDTO(
                5L,
                "Title_1",
                "Description_1",
                new Date(System.currentTimeMillis() + 10_000),
                new Date(System.currentTimeMillis() + 90_000),
                "Republican",
                true
        );
        //when
        ResponseEntity<EventDTO> response =
                testRestTemplate.postForEntity(
                        "/events",
                        eventDTO,
                        EventDTO.class
                );
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void updateEvents_withExistingEvent_shouldProperlyUpdateEvent() {
        //given
        EventDTO eventDTO = new EventDTO(
                4L,
                "Title_1",
                "Description_1",
                new Date(System.currentTimeMillis() + 10_000),
                new Date(System.currentTimeMillis() + 90_000),
                "Republican",
                true
        );
        HttpEntity<EventDTO> entity = new HttpEntity<>(eventDTO);
        //when
        ResponseEntity<EventDTO> response =
                testRestTemplate.exchange(
                        "/events",
                        HttpMethod.PUT,
                        entity,
                        EventDTO.class
                );
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}