package by.belyahovich.dance_events.dto;

import by.belyahovich.dance_events.domain.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {

    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getId(),
                user.getLogin(),
                user.getPassword(),
                user.isActive(),
                user.getUserInfo(),
                user.getRole().getRoleTitle()
        );
    }

}
