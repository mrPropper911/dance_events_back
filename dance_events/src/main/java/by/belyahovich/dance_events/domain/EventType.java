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

    @OneToMany(targetEntity = Event.class, mappedBy = "eventType", fetch = FetchType.LAZY)
    private Set<Event> eventsByType;

    public EventType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventType eventType = (EventType) o;

        if (id != eventType.id) return false;
        if (!Objects.equals(type, eventType.type)) return false;
        return Objects.equals(eventsByType, eventType.eventsByType);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (eventsByType != null ? eventsByType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EventType{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", eventsByType=" + eventsByType +
                '}';
    }
}