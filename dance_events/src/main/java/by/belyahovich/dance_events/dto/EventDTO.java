package by.belyahovich.dance_events.dto;

import java.util.Date;

public record EventDTO(
        Long id,
        String title,
        String description,
        Date startDate,
        Date endDate,
        String eventTypeTitle,
        boolean active
) {
}
