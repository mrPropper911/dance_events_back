package by.belyahovich.dance_events.controller.authorization;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public record ProfileRequest(
        @NotNull
        String login,
        @NotNull
        @Min(5)
        String password,
        @NotNull
        String roleTitle
) {
}