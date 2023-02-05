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
import java.util.Set;

@RestController
public class RegistrationController {

    private final UserService userService;
    private final RoleService roleService;
    private final JwtProvider jwtProvider;


    @Autowired
    public RegistrationController(
            UserService userService,
            RoleService roleService,
            JwtProvider jwtProvider)
    {
        this.userService = userService;
        this.roleService = roleService;
        this.jwtProvider = jwtProvider;
    }

    /**
     * <h2>Get all title of role from database</h2>
     *
     * @return {@link HttpStatus} and set of {@link Role}
     */
    @GetMapping("/signup")
    public ResponseEntity<?> getRoleForSignUp() {
        Set<String> rolesFromDb = roleService.findAllRole();
        return new ResponseEntity<>(rolesFromDb, HttpStatus.OK);
    }

    /**
     * <h2>Sign Up</h2>
     * Save new user in database<p>
     * To create a new user, the following parameters are required:<p>
     * - login (new uniq login for user)<p>
     * - password (new password > 5 elements)<p>
     * - roleTitle (must start with "ROLE_")<p>
     *
     * @param request {@link ProfileRequest}
     * @return {@link HttpStatus}
     */
    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody @Valid ProfileRequest request) {
        userService.createUser(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * <h2>Sign In</h2>
     * To authentithication, the following parameters are required:
     *
     * @param accountCredentials {@link AccountCredentials}
     * @return {@link HttpStatus} and {@link JwtProvider}
     */
    @PostMapping(value = "/signin")
    public ResponseEntity<?> signIn(@RequestBody AccountCredentials accountCredentials) {
        userService.findUserByLoginAndPassword(accountCredentials.login(), accountCredentials.password());
        String token = jwtProvider.generateToken(accountCredentials.login());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
                .build();
    }
}
