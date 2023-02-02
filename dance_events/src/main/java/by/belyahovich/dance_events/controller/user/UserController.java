package by.belyahovich.dance_events.controller.user;

import by.belyahovich.dance_events.domain.User;
import by.belyahovich.dance_events.domain.UserInfo;
import by.belyahovich.dance_events.service.user.UserService;
import by.belyahovich.dance_events.service.userinfo.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;
    private final UserInfoService userInfoService;

    @Autowired
    public UserController(UserService userService, UserInfoService userInfoService) {
        this.userService = userService;
        this.userInfoService = userInfoService;
    }

    /**
     * Getting user information
     *
     * @param login {@link User}
     *              To getting user information, the following parameters are required:
     *              - String login (variable in URL)
     * @return UserInfo + {@link HttpStatus}
     */
    @GetMapping("/users/{login}")
    public ResponseEntity<?> getUserInfo(@PathVariable String login) {
        try {
            UserInfo userInfoByUserId = userInfoService.findUserInfoByUserLogin(login);
            return new ResponseEntity<>(userInfoByUserId, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Adding and changing user information
     *
     * @param userInfo {@link UserInfo}
     * @param login    {@link User}
     *                 To create/update user information, the following parameters are required:
     *                 - String login (variable in URL)
     *                 - String name
     *                 - String surname
     *                 - String phone
     *                 - String email
     * @return {@link HttpStatus}
     */
    @PostMapping("/users/{login}")
    public ResponseEntity<?> updateUserInfo(@RequestBody UserInfo userInfo, @PathVariable String login) {
        try {
            UserInfo userInfoByUserId = userInfoService.findUserInfoByUserLogin(login);
            UserInfo userInfoToSave = new UserInfo(userInfoByUserId.getId(), userInfo.getName(), userInfo.getSurname(),
                    userInfo.getPhone(), userInfo.getEmail());
            userInfoService.createUserInfo(userInfoToSave);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deleting a user and his data using the active flag
     * Example request in postman: localhost:8080/users/user123?active=1
     * Params: active 1
     * Body: raw(JSON) true/false
     *
     * @param active {@link User}
     * @param login  {@link User}
     *               To change activity, the following parameters are required:
     *               - String login (variable in URL)
     *               - boolean active (false for delete)
     * @return {@link HttpStatus}
     */
    @PatchMapping("/users/{login}")
    public ResponseEntity<?> updateUserActive(@RequestBody boolean active, @PathVariable String login) {
        try {
            userService.updateUserActive(login, active);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}






















