package by.belyahovich.dance_events.dto;

import by.belyahovich.dance_events.domain.UserInfo;

public record UserDTO(
        Long id,
        String login,
        String password,
        boolean active,
        UserInfo userInfo,
        String roleTitle
) {

}
