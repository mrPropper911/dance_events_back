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
     * <h2>Getting a list of all events (sorted by date)</h2>
     *
     * @return list of {@link EventDTO} and {@link HttpStatus}
     */
    @GetMapping("/events")
    public ResponseEntity<?> getAllEvents() {
        List<EventDTO> allEvents = eventService.findAllEventsSortedByStartDate();
        return new ResponseEntity<>(allEvents, HttpStatus.OK);
    }

    /**
     * <h2>Search events like title</h2>
     * Example request in postman: localhost:8080/events/search?title=1<p>
     * To search event, the following parameters are required:
     *
     * @param title {@link EventDTO}
     * @return {@link HttpStatus} and list of like title Events
     */
    @GetMapping("/events/search")
    public ResponseEntity<?> getEventsLikeTitle(@RequestBody String title) {
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
    @GetMapping("/events/type")
    public ResponseEntity<?> getEventsByEventsType(@RequestBody String eventType) {
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
        return new ResponseEntity<>(HttpStatus.OK);
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
