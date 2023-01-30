package by.belyahovich.dance_events.controller.authorization;

import by.belyahovich.dance_events.domain.Role;
import by.belyahovich.dance_events.domain.User;
import by.belyahovich.dance_events.service.role.RoleService;
import by.belyahovich.dance_events.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@RestController
public class RegistrationController {

    private final UserService userService;
    private final RoleService roleService;


    @Autowired
    public RegistrationController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
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
     * @return {@link HttpStatus} 201 - created,400 - error, not created
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
     * Needed to add a role when creating a user
     *
     * @return {@link HttpStatus} and Set"Role"
     */
    @GetMapping("/signup")
    public ResponseEntity<?> signUp() {
        try {
            Set<Role> rolesFromDb = roleService.findAllRole();
            return new ResponseEntity<>(rolesFromDb, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

//    @PostMapping(value = "/signin")
//    public ResponseEntity<?> signIn(@RequestBody AccountCredentials accountCredentials){
//        UsernamePasswordAuthenticationToken creds =
//                new UsernamePasswordAuthenticationToken(
//                    accountCredentials.login(), accountCredentials.password()
//                );
//        Authentication authentication = authenticationManager.authenticate(creds);
//        String jwts = jwtTokenRepository.generateToken()
//    }

    @PostMapping("/signin")
    public @ResponseBody by.belyahovich.dance_events.domain.User signIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        User user = (principal instanceof User) ? (User) principal : null;
        return Objects.nonNull(user) ? this.userService.findUserByLogin(user.getUsername()).orElseThrow() : null;
    }


}
