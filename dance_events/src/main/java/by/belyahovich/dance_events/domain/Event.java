package by.belyahovich.dance_events.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "endDate")
    private Date endDate;

    @Column(name = "description")
    private String description;

    @Column(name = "active")
    private boolean active;

    @ManyToMany(mappedBy = "likedEvents", fetch = FetchType.LAZY)
    private Set<User> likedByUser;

    @ManyToOne
    @JoinColumn(name = "eventsByType", referencedColumnName = "id")
    private EventType eventType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (id != event.id) return false;
        if (active != event.active) return false;
        if (!Objects.equals(title, event.title)) return false;
        if (!Objects.equals(startDate, event.startDate)) return false;
        if (!Objects.equals(endDate, event.endDate)) return false;
        if (!Objects.equals(description, event.description)) return false;
        if (!Objects.equals(likedByUser, event.likedByUser)) return false;
        return Objects.equals(eventType, event.eventType);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + (likedByUser != null ? likedByUser.hashCode() : 0);
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", description='" + description + '\'' +
                ", active=" + active +
                ", likedByUser=" + likedByUser +
                ", eventType=" + eventType +
                '}';
    }
}
