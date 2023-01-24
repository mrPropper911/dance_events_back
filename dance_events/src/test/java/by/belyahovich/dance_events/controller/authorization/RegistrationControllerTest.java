
package by.belyahovich.dance_events.controller.authorization;

import by.belyahovich.dance_events.domain.Role;
import by.belyahovich.dance_events.domain.User;
import by.belyahovich.dance_events.repository.role.RoleRepository;
import by.belyahovich.dance_events.service.role.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegistrationControllerTest {

    private static final String SOME_LOGIN = "SOME_LOGIN";
    private static final String SOME_PASSWORD = "SOME_PASSWORD";
    private static final String SOME_ROLE_TITLE = "SOME_ROLE_TITLE";

    private Role role;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void init(){
        User user = new User();
        user.setLogin(SOME_LOGIN);
        user.setPassword(SOME_PASSWORD);

        role = new Role();
        role.setRoleTitle(SOME_ROLE_TITLE);
        Role actualRole = roleService.createRole(role);
    }

    @Test
    void signUp_whenCreateUser_thenStatus201() {
        //given
//        User user = new User();
//        user.setLogin("SOME_LOGIN_RANDOM");
//        user.setPassword("SOME_PASSWORD_RANDOM");
//        user.setRole(role);
        ProfileRequest profileRequest = new ProfileRequest("SOME_LOGIN_RANDOM",
                "SOME_PASSWORD_RANDOM", SOME_ROLE_TITLE);

        ProfileRequest profileRequest1 = testRestTemplate.postForObject("/signup", profileRequest, ProfileRequest.class);
        //when
//        ResponseEntity<User> actualResponse = testRestTemplate.postForEntity("/signup", user, User.class);
        //then
//        assertThat(actualResponse).isEqualTo(HttpStatus.CREATED);

    }
}