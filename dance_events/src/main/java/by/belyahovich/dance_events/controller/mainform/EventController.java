package by.belyahovich.dance_events.controller.mainform;

import by.belyahovich.dance_events.config.ResourceNotFoundException;
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
     * Getting a list of all events (sorted by date)
     * All events for main form
     *
     * @return list of {@link EventDTO} and {@link HttpStatus}
     */
    @GetMapping("/events")
    public ResponseEntity<?> getAllEvents() {
        try {
            List<EventDTO> allEvents = eventService.findAllEvents();
            return new ResponseEntity<>(allEvents, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Search events like title
     * Example request in postman: localhost:8080/events/search?title=1
     *
     * @param title {@link EventDTO}
     *              To search event, the following parameters are required:
     *              - String title
     * @return {@link HttpStatus} and list of Event
     */
    @GetMapping("/events/search")
    public ResponseEntity<?> getEventsLikeTitle(@RequestBody String title) {
        List<EventDTO> eventLikeTitle = eventService.findEventLikeTitle(title);
        if (eventLikeTitle.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(eventLikeTitle, HttpStatus.OK);
        }
    }


    /**
     * Sorting events by event type
     *
     * @param eventType title
     *                  To sorting by event type, the following parameters are required:
     *      *              - String eventType
     * @return {@link HttpStatus} and sorting list
     */
    @GetMapping("/events/type")
    public ResponseEntity<?> getEventsByEventsType (@RequestBody String eventType){
        try {
            return new ResponseEntity<>(eventService.findEventByEventType(eventType),
                    HttpStatus.OK);
        } catch (Exception e){
            if (e.getClass().equals(ResourceNotFoundException.class)) {
                return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }

    /**
     * Create new event
     * Save new event to database
     *
     * @param newEvent {@link EventDTO}
     *                 To create a new event, the following parameters are required:
     *                 - String title
     *                 - Date description
     *                 - Date startDate
     *                 - Date endDate
     *                 - String eventTypeTitle
     * @return {@link EventDTO} and {@link HttpStatus}
     * What statuses can return:
     * - 200 success
     * - 205 if events exist
     * - 400 had any event
     */
    @PostMapping("/events")
    public ResponseEntity<?> createNewEvent(@RequestBody EventDTO newEvent) {
        try {
            return new ResponseEntity<>(eventService.createNewEvent(newEvent),
                    HttpStatus.OK);
        } catch (Exception e) {
            if (e.getClass().equals(ResourceNotFoundException.class)) {
                return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }

    /**
     * Update events or delete his data using the active flag
     * For delete event you should set active field to false
     *
     * @param updateEvent {@link EventDTO}
     *                    To update/delete event, the following parameters are required:
     *                    - Long id
     *                    - String title
     *                    - String description
     *                    - Date startDate
     *                    - Date endDate
     *                    - String eventTypeTitle
     *                    - boolean active
     * @return {@link HttpStatus}
     */
    @PutMapping("/events")
    public ResponseEntity<?> updateEvents(@RequestBody EventDTO updateEvent) {
        try {
            eventService.updateEvent(updateEvent);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }
}
