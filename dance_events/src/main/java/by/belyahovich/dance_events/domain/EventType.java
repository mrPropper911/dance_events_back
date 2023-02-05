package by.belyahovich.dance_events.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "events_type")
public class EventType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "type")
    private String type;

    @OneToMany(mappedBy = "eventType")
    private Set<Event> eventsByType;

    public EventType(long id, String type) {
        this.id = id;
        this.type = type;
    }
}