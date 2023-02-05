package by.belyahovich.dance_events.service.user;

import by.belyahovich.dance_events.controller.authorization.ProfileRequest;
import by.belyahovich.dance_events.domain.Event;
import by.belyahovich.dance_events.domain.User;
import by.belyahovich.dance_events.dto.EventDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> allUsers();

    Optional<User> findUserByLogin(String login);

    void findUserByLoginAndPassword(String login, String password);

    void createUser(ProfileRequest request);

    void deleteUser(User user);

    void updateUserActive (Long userId, boolean active);

    List<EventDTO> getAllLikedUserEventsSortedByStartDate (Long userId);

    void addLikeEventToUser(Long userId, Long eventId);

    void deleteLikedEvent(Long userId, Long eventId);
}
