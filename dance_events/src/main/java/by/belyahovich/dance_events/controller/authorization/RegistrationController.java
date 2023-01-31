package by.belyahovich.dance_events.controller.authorization;

import by.belyahovich.dance_events.domain.Role;
import by.belyahovich.dance_events.domain.User;
import by.belyahovich.dance_events.security.JwtProvider;
import by.belyahovich.dance_events.service.role.RoleService;
import by.belyahovich.dance_events.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

@RestController
public class RegistrationController {

    private final UserService userService;
    private final RoleService roleService;
    private final JwtProvider jwtProvider;


    @Autowired
    public RegistrationController(UserService userService, RoleService roleService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.roleService = roleService;
        this.jwtProvider = jwtProvider;
    }

    /**
     * SignUp new user
     * Create new user in database
     *
     * @param request {@link ProfileRequest}
     *                To create a new user, the following parameters are required:
     *                - login (new uniq login for user)
     *                - password (new password > 5 elements)
     *                - roleTitle (choose role of user)
     * @return {@link HttpStatus} 201 - created, 400 - error, not created
     */
    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody @Valid ProfileRequest request) {
        User userForAddToDatabase = new User();
        userForAddToDatabase.setLogin(request.login());
        userForAddToDatabase.setPassword(request.password());
        userForAddToDatabase.setActive(true);
        //Create Role entity from Database and add this Role to new User
        Optional<Role> roleByTitle = roleService.findRoleByTitle(request.roleTitle());
        userForAddToDatabase.setRole(roleByTitle.orElseThrow());
        //Try to save user in database
        try {
            userService.createUser(userForAddToDatabase);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get All role from database
     * Needed to choose a role when creating a user
     *
     * @return {@link HttpStatus} and Set<Role>
     */
    @GetMapping("/signup")
    public ResponseEntity<?> getRoleForSignUp() {
        try {
            Set<Role> rolesFromDb = roleService.findAllRole();
            return new ResponseEntity<>(rolesFromDb, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * User authentithication
     *
     * @param accountCredentials {@link AccountCredentials}
     *                           To authentithication, the following parameters are required:
     *                           - login
     *                           - password
     * @return {@link HttpStatus} jwt token.
     */
    @PostMapping(value = "/signin")
    public ResponseEntity<?> signIn(@RequestBody AccountCredentials accountCredentials) {
        Optional<User> userByLogin =
                userService.findUserByLoginAndPassword(accountCredentials.login(), accountCredentials.password());
        String token = jwtProvider.generateToken(userByLogin.orElseThrow().getLogin());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
                .build();
    }
}
