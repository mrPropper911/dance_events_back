package by.belyahovich.dance_events.repository.event;

import by.belyahovich.dance_events.domain.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository <Event, Long> {
}
