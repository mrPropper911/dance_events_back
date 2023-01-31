package by.belyahovich.dance_events.service.user;

import by.belyahovich.dance_events.domain.Event;
import by.belyahovich.dance_events.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> allUsers();

    Optional<User> findUserByLogin(String login);

    Optional<User> findUserByLoginAndPassword(String login, String password);

    User createUser(User user);

    void deleteUser(User user);

    void updateUserActive (String login, boolean active);

    List<Event> getAllLikedUserEventsByUser (User user);

}
