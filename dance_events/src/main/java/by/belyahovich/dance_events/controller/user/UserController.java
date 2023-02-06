package by.belyahovich.dance_events.controller.user;

import by.belyahovich.dance_events.domain.Event;
import by.belyahovich.dance_events.domain.User;
import by.belyahovich.dance_events.dto.EventDTO;
import by.belyahovich.dance_events.dto.UserInfoDTO;
import by.belyahovich.dance_events.service.user.UserService;
import by.belyahovich.dance_events.service.userinfo.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class UserController {

    private final UserService userService;
    private final UserInfoService userInfoService;

    @Autowired
    public UserController(
            UserService userService,
            UserInfoService userInfoService
    ) {
        this.userService = userService;
        this.userInfoService = userInfoService;
    }

    /**
     * <h2>Getting user information by user ID</h2>
     * To getting user information, the following parameters are required:
     *
     * @param userId {@link User} (variable in URL)
     * @return {@link UserInfoDTO} + {@link HttpStatus}
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserInfoByUserId(@PathVariable Long userId) {
        UserInfoDTO userInfoByUserId = userInfoService.findUserInfoByUserId(userId);
        return new ResponseEntity<>(userInfoByUserId, HttpStatus.OK);
    }

    /**
     * <h2>Update user information by user ID</h2>
     * To create/update user information, the following parameters are required:
     *
     * @param userInfoDTO {@link UserInfoDTO}
     * @param userId      {@link User} (variable in URL)
     * @return {@link HttpStatus}
     */
    @PostMapping("/users/{userId}")
    public ResponseEntity<?> updateUserInfoByUserId(@RequestBody UserInfoDTO userInfoDTO, @PathVariable Long userId) {
        userInfoService.saveUserInfo(userId, userInfoDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * <h2>Update user activity (change the active flag)</h2>
     * Example request in postman:<p>
     * - URL: localhost:8080/users/2?active=1<p>
     * - Params: active 1<p>
     * - Body: raw(JSON) true/false<p>
     * To change activity, the following parameters are required:
     *
     * @param active {@link User} (false - is not active)
     * @param userId {@link User} (variable in URL)
     * @return {@link HttpStatus}
     */
    @PutMapping("/users/{userId}")
    public ResponseEntity<?> updateUserActive(@RequestBody boolean active, @PathVariable Long userId) {
        userService.updateUserActive(userId, active);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * <h2>Getting a list of liked events sorted by his start date</h2>
     * To get all liked events, the following parameters are required:
     *
     * @param userId {@link User} (variable in URL)
     * @return list of {@link EventDTO} and {@link HttpStatus}
     */
    @GetMapping("/users/{userId}/liked")
    public ResponseEntity<?> getAllLikedEventOfUserSortedByStartDate(@PathVariable Long userId) {
        Set<EventDTO> allLikedUserEvents = userService.getAllLikedUserEventsSortedByStartDate(userId);
        return new ResponseEntity<>(allLikedUserEvents, HttpStatus.OK);
    }

    /**
     * <h2>Add user liked event to user</h2>
     * To add event to liked user, the following parameters are required:
     *
     * @param userId  {@link User} (variable in URL)
     * @param eventId {@link Event} (variable in URL)
     * @return {@link HttpStatus}
     */
    @PostMapping("/users/{userId}/liked/{eventId}")
    public ResponseEntity<?> addLikeEventToUser(@PathVariable Long userId, @PathVariable Long eventId) {
        userService.addLikeEventToUser(userId, eventId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * <h2>Delete user liked event from user</h2>
     * To delete liked event from user, the following parameters are required:
     *
     * @param userId  {@link User} (variable in URL)
     * @param eventId {@link Event} (variable in URL)
     * @return {@link HttpStatus}
     */
    @DeleteMapping("/users/{userId}/liked/{eventId}")
    public ResponseEntity<?> deleteLikeEventFromUser(@PathVariable Long userId, @PathVariable Long eventId) {
        userService.deleteLikedEvent(userId, eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}