package by.belyahovich.dance_events.service.user;

import by.belyahovich.dance_events.domain.Event;
import by.belyahovich.dance_events.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> allUsers();

    Optional<User> findUserByLogin(String login);

    User createUser(User user);

    void deleteUser(User user);

    List<Event> getAllLikedUserEventsByUser (User user);

}
