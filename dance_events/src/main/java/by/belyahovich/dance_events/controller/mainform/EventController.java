package by.belyahovich.dance_events.controller.mainform;

import by.belyahovich.dance_events.domain.EventType;
import by.belyahovich.dance_events.dto.EventDTO;
import by.belyahovich.dance_events.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * <h2>Getting a list of all events (sorted by date) and Pageable</h2>
     * Example request:<p>
     * - localhost:8080/events (by default first(0)page two entity on page)<p>
     * - localhost:8080/events?pageSize=5<p>
     * - localhost:8080/events?pageSize=1&pageNo=1<p>
     *
     * @return list of {@link EventDTO} and {@link HttpStatus}
     */
    @GetMapping("/events")
    public ResponseEntity<?> getAllEventsSortedByStartDate(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "2") Integer pageSize) {
        List<EventDTO> allEvents = eventService.findAllEventsSortedByStartDate(pageNo, pageSize);
        return new ResponseEntity<>(allEvents, HttpStatus.OK);
    }

    /**
     * <h2>Search events like title</h2>
     * Example request in postman: localhost:8080/events/title-search?title=AnyTitle<p>
     * To search event, the following parameters are required:
     *
     * @param title {@link EventDTO}
     * @return {@link HttpStatus} and list of like title Events
     */
    @GetMapping("/events/title-search")
    public ResponseEntity<?> getEventsLikeTitle(@RequestParam String title) {
        List<EventDTO> eventLikeTitle = eventService.findEventLikeTitle(title);
        return new ResponseEntity<>(eventLikeTitle, HttpStatus.OK);
    }

    /**
     * <h2>Getting a list of events (sorting by event type)</h2>
     * To sorting by event type, the following parameters are required:
     *
     * @param eventType title of {@link EventType}
     * @return {@link HttpStatus} and sorting list of {@link EventDTO}
     */
    @GetMapping("/events/type-search")
    public ResponseEntity<?> getEventsByEventsType(@RequestParam String eventType) {
        List<EventDTO> eventByEventType = eventService.findEventByEventType(eventType);
        return new ResponseEntity<>(eventByEventType, HttpStatus.OK);
    }

    /**
     * <h2>Create new event</h2>
     * Save new event to database
     * To create a new event, the following parameters are required:
     *
     * @param newEvent {@link EventDTO} (ID not need)
     * @return {@link EventDTO} and {@link HttpStatus}
     */
    @PostMapping("/events")
    public ResponseEntity<?> createNewEvent(@RequestBody EventDTO newEvent) {
        eventService.createNewEvent(newEvent);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * <h2>Update events or change activity</h2>
     * For change activity of event you should set active field to false <p>
     * To update/change activity event, the following parameters are required:
     *
     * @param updateEvent {@link EventDTO}
     * @return {@link HttpStatus}
     */
    @PutMapping("/events")
    public ResponseEntity<?> updateEvents(@RequestBody EventDTO updateEvent) {
        eventService.updateEvent(updateEvent);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}