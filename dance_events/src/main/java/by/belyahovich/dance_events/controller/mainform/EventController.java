package by.belyahovich.dance_events.controller.mainform;

import by.belyahovich.dance_events.domain.Event;
import by.belyahovich.dance_events.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllEvents(){
        try {
            List<Event> allEvents = eventService.findAllEvents();
            return new ResponseEntity<>(allEvents, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }


}
