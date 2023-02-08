package by.belyahovich.dance_events.controller.authorization;

import by.belyahovich.dance_events.domain.Role;
import by.belyahovich.dance_events.domain.User;
import by.belyahovich.dance_events.repository.role.RoleRepository;
import by.belyahovich.dance_events.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
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

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private RoleRepository repositoryRole;
    @Autowired
    private UserRepository repositoryUser;

    @BeforeEach
    public void resetDb(){
        repositoryUser.deleteAll();
        repositoryRole.deleteAll();
    }

    @Test
    public void getRoleForSignUp_withExistingRole_shouldProperlyReturnRole() {
        //given
        repositoryRole.saveAll(List.of(
                new Role(1L, "ROLE_ADMINISTRATOR"),
                new Role(2L, "ROLE_ORGANIZER"),
                new Role(3L, "ROLE_MEMBER")
        ));
        ResponseEntity<String> response =
                testRestTemplate.exchange(
                        "/signup",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String actualEvents = response.getBody();
        assertThat(actualEvents).isEqualTo("[\"ROLE_MEMBER\",\"ROLE_ADMINISTRATOR\",\"ROLE_ORGANIZER\"]");
    }

    @Test
    public void signUpPOST_withCreateNotExistingUser_shouldProperlyReturnStatus201() {
        //given
        repositoryRole.saveAll(List.of(
                new Role(1L, "ROLE_ADMINISTRATOR"),
                new Role(2L, "ROLE_ORGANIZER"),
                new Role(3L, "ROLE_MEMBER")
        ));
        ProfileRequest profileRequest = new ProfileRequest(
                "SOME_LOGIN_RANDOM",
                "SOME_PASSWORD_RANDOM",
                "ROLE_ORGANIZER");
        //when
        ResponseEntity<ProfileRequest> profileRequestResponseEntity =
                testRestTemplate.postForEntity(
                        "/signup",
                        profileRequest,
                        ProfileRequest.class
                );
        //then
        assertThat(profileRequestResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void signUpPOST_withExistingUser_shouldProperlyReturnStatus404() {
        //given
        String EXISTING_LOGIN = "lovanda";
        Role roleAdministrator = repositoryRole.save(new Role(1L, "ROLE_ADMINISTRATOR"));
        repositoryUser.save(new User(
                1L,
                EXISTING_LOGIN,
                "$2a$10$xVejl2R42O/ACWSnQNDU1ec0fx4MTgfMGcx.PTaMca58uarJx8xAi",
                true,
                roleAdministrator
        ));
        ProfileRequest profileRequest =
                new ProfileRequest(
                        "EXISTING_LOGIN",
                        "SOME_PASSWORD",
                        "ROLE_ORGANIZER");
        //when
        ResponseEntity<ProfileRequest> profileRequestResponseEntity =
                testRestTemplate.postForEntity(
                        "/signup",
                        profileRequest,
                        ProfileRequest.class);
        //then
        assertThat(profileRequestResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void signIn_withExistingUser_shouldProperlySignInUser() {
        //given
        String EXISTING_LOGIN = "lovanda";
        String EXISTING_PASSWORD = "hardPass";
        Role roleAdministrator = repositoryRole.save(new Role(1L, "ROLE_ADMINISTRATOR"));
        repositoryUser.save(new User(
                1L,
                EXISTING_LOGIN,
                "$2a$10$xVejl2R42O/ACWSnQNDU1ec0fx4MTgfMGcx.PTaMca58uarJx8xAi",
                true,
                roleAdministrator
        ));

        AccountCredentials accountCredentials = new AccountCredentials(EXISTING_LOGIN, EXISTING_PASSWORD);
        //when
        ResponseEntity<AccountCredentials> response =
                testRestTemplate.postForEntity(
                        "/signin",
                        accountCredentials,
                        AccountCredentials.class
                );
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().get(HttpHeaders.AUTHORIZATION)).isNotEmpty();
    }

    @Test
    public void signIn_withBadPassword_shouldDoesNotSignIn() {
        //given
        String EXISTING_LOGIN = "lovanda";
        Role roleAdministrator = repositoryRole.save(new Role(1L, "ROLE_ADMINISTRATOR"));
        repositoryUser.save(new User(
                1L,
                EXISTING_LOGIN,
                "$2a$10$xVejl2R42O/ACWSnQNDU1ec0fx4MTgfMGcx.PTaMca58uarJx8xAi",
                true,
                roleAdministrator
        ));
        AccountCredentials accountCredentials = new AccountCredentials("lovanda", "qwer");
        //when
        ResponseEntity<AccountCredentials> response =
                testRestTemplate.postForEntity(
                        "/signin",
                        accountCredentials,
                        AccountCredentials.class
                );
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getHeaders().get(HttpHeaders.AUTHORIZATION)).isNullOrEmpty();
    }
}