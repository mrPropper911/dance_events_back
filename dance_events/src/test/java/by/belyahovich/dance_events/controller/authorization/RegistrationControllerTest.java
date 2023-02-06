package by.belyahovich.dance_events.controller.authorization;

import by.belyahovich.dance_events.domain.Role;
import by.belyahovich.dance_events.domain.User;
import by.belyahovich.dance_events.service.role.RoleService;
import by.belyahovich.dance_events.service.user.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RegistrationController integration test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegistrationControllerTest {

    private static final String SOME_LOGIN = "SOME_LOGIN";
    private static final String SOME_PASSWORD = "SOME_PASSWORD";
    private static final String SOME_ROLE_TITLE = "SOME_ROLE_TITLE";

    protected User actualUser = new User();

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @BeforeAll
    public void init() {
        User user = new User();
        user.setLogin(SOME_LOGIN);
        user.setPassword(SOME_PASSWORD);
        //create new Role add to new User
        Role role = new Role();
        role.setRoleTitle(SOME_ROLE_TITLE);
        Role actualRole = roleService.createRole(role);
        user.setRole(actualRole);
        //save new User to database
//        actualUser = userService.createUser(user);
    }

    @Test
    public void signUpPOST_withCreateNotExistingUser_shouldProperlyReturnStatus201() {
        //given
        String SOME_LOGIN_RANDOM = "SOME_LOGIN_RANDOM";
        String SOME_PASSWORD_RANDOM = "SOME_PASSWORD_RANDOM";

        ProfileRequest profileRequest =
                new ProfileRequest(SOME_LOGIN_RANDOM, SOME_PASSWORD_RANDOM, SOME_ROLE_TITLE);
        //when
        ResponseEntity<ProfileRequest> profileRequestResponseEntity =
                testRestTemplate.postForEntity("/signup", profileRequest, ProfileRequest.class);
        //then
        assertThat(profileRequestResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void signUpPOST_withExistingUser_shouldProperlyReturnStatus400() {
        //given
        ProfileRequest profileRequest =
                new ProfileRequest(SOME_LOGIN, SOME_PASSWORD, SOME_ROLE_TITLE);
        //then
        ResponseEntity<ProfileRequest> profileRequestResponseEntity =
                testRestTemplate.postForEntity("/signup", profileRequest, ProfileRequest.class);

        assertThat(profileRequestResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void signUpGET_withExistingRole_shouldProperlyFindAllRoleAndReturnSetOfRoleAndStatus200() {
        //given
        ResponseEntity<List<Role>> response =
                testRestTemplate.exchange("/signup", HttpMethod.GET, null,
                        new ParameterizedTypeReference<>() {
                        });
        //then
        List<Role> actualRole = response.getBody();
        assertThat(actualRole).hasSize(1);
        assertThat(actualRole.get(0).getRoleTitle()).isEqualTo(SOME_ROLE_TITLE);
    }
}