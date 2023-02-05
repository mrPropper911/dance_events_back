package by.belyahovich.dance_events.controller.mainform;

import by.belyahovich.dance_events.domain.Event;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Events controller unit-test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EventControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

//    @Test
//    void getAllEvents_withExistingEvents_shouldProperlyFindAllEvents() {
//        //given
//        ResponseEntity<List<Event>> response =
//                testRestTemplate.exchange("/", HttpMethod.GET, null,
//                        new ParameterizedTypeReference<>() {
//                        });
//        //then
//        List<Event> actualEvents = response.getBody();
//        assertThat(actualEvents).hasSize(4);
//
//    }
}