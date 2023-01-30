package by.belyahovich.dance_events.controller.authorization;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public record AccountCredentials (
        @NotNull
        String login,
        @NotNull
        @Min(5)
        String password
) {
}
