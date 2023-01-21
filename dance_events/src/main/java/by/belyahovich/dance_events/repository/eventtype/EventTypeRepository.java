package by.belyahovich.dance_events.repository.eventtype;

import by.belyahovich.dance_events.domain.EventType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventTypeRepository extends CrudRepository<EventType, Long> {
}
