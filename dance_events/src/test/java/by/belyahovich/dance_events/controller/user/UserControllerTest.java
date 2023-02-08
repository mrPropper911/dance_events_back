package by.belyahovich.dance_events.controller.user;

import by.belyahovich.dance_events.controller.authorization.AccountCredentials;
import by.belyahovich.dance_events.dto.EventDTO;
import by.belyahovich.dance_events.dto.UserInfoDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserController integration test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void getUserInfoByUserId_withExistingUser_shouldProperlyFindUserInfo() {
        //authenticate
        String EXISTING_LOGIN = "lovanda";
        String EXISTING_PASSWORD = "hardPass";
        AccountCredentials accountCredentials =
                new AccountCredentials(EXISTING_LOGIN, EXISTING_PASSWORD);
        ResponseEntity<AccountCredentials> responseForAuth =
                testRestTemplate.postForEntity(
                        "/signin",
                        accountCredentials,
                        AccountCredentials.class
                );
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", responseForAuth.getHeaders().getFirst("Authorization"));
        //given
        String url = "/users/{userId}";
        Map<String, Long> urlParams = new HashMap<>();
        urlParams.put("userId", 1L);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        //when
        ResponseEntity<UserInfoDTO> response =
                testRestTemplate.exchange(
                        builder.buildAndExpand(urlParams).toUriString(),
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        UserInfoDTO.class
                );
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void updateUserInfoByUserId_withExistingUser_shouldProperlyUpdateUser() {
        //authenticate
        String EXISTING_LOGIN = "lovanda";
        String EXISTING_PASSWORD = "hardPass";
        AccountCredentials accountCredentials =
                new AccountCredentials(EXISTING_LOGIN, EXISTING_PASSWORD);

        ResponseEntity<AccountCredentials> responseForAuth =
                testRestTemplate.postForEntity(
                        "/signin",
                        accountCredentials,
                        AccountCredentials.class
                );
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", responseForAuth.getHeaders().getFirst("Authorization"));
        //given
        String url = "/users/{userId}";

        Map<String, Long> urlParams = new HashMap<>();
        urlParams.put("userId", 1L);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

        UserInfoDTO userInfoDTO = new UserInfoDTO(
                1L,
                "NAME",
                "SURNAME",
                "PHONE",
                "EMAIL"
        );
        //when
        ResponseEntity<UserInfoDTO> response =
                testRestTemplate.exchange(
                        builder.buildAndExpand(urlParams).toUriString(),
                        HttpMethod.POST,
                        new HttpEntity<>(userInfoDTO, headers),
                        UserInfoDTO.class
                );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void updateUserActive_withExistingUser_shouldProperlyUpdate() {
        //authenticate
        String EXISTING_LOGIN = "lovanda";
        String EXISTING_PASSWORD = "hardPass";
        AccountCredentials accountCredentials =
                new AccountCredentials(EXISTING_LOGIN, EXISTING_PASSWORD);

        ResponseEntity<AccountCredentials> responseForAuth =
                testRestTemplate.postForEntity(
                        "/signin",
                        accountCredentials,
                        AccountCredentials.class
                );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", responseForAuth.getHeaders().getFirst("Authorization"));

        String url = "/users/{userId}";

        Map<String, Long> urlParams = new HashMap<>();
        urlParams.put("userId", 1L);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("active", true);

        //when
        ResponseEntity<UserInfoDTO> response =
                testRestTemplate.exchange(
                        builder.buildAndExpand(urlParams).toUriString(),
                        HttpMethod.PUT,
                        new HttpEntity<>(headers),
                        UserInfoDTO.class
                );
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getAllLikedEventOfUserSortedByStartDate() {
        //authenticate
        String EXISTING_LOGIN = "lovanda";
        String EXISTING_PASSWORD = "hardPass";
        AccountCredentials accountCredentials =
                new AccountCredentials(EXISTING_LOGIN, EXISTING_PASSWORD);

        ResponseEntity<AccountCredentials> responseForAuth =
                testRestTemplate.postForEntity(
                        "/signin",
                        accountCredentials,
                        AccountCredentials.class
                );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", responseForAuth.getHeaders().getFirst("Authorization"));
        //given
        String url = "/users/{userId}/liked";

        Map<String, Long> urlParams = new HashMap<>();
        urlParams.put("userId", 1L);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        //when
        ResponseEntity<Set<EventDTO>> response =
                testRestTemplate.exchange(
                        builder.buildAndExpand(urlParams).toUriString(),
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void addLikeEventToUser_withExistingUserAndEvent_shouldProperlyAddEventToLikeUser() {
        //authenticate
        String EXISTING_LOGIN = "lovanda";
        String EXISTING_PASSWORD = "hardPass";
        AccountCredentials accountCredentials =
                new AccountCredentials(EXISTING_LOGIN, EXISTING_PASSWORD);

        ResponseEntity<AccountCredentials> responseForAuth =
                testRestTemplate.postForEntity(
                        "/signin",
                        accountCredentials,
                        AccountCredentials.class
                );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", responseForAuth.getHeaders().getFirst("Authorization"));
        //given
        String url = "/users/{userId}/liked/{eventId}";

        Map<String, Long> urlParams = new HashMap<>();
        urlParams.put("userId", 1L);
        urlParams.put("eventId", 4L);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        //when
        ResponseEntity<?> response =
                testRestTemplate.exchange(
                        builder.buildAndExpand(urlParams).toUriString(),
                        HttpMethod.POST,
                        new HttpEntity<>(headers),
                        EventDTO.class
                );
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void deleteLikeEventFromUser_withExistingUserAndEvent_shouldProperlyDeleteLikeEvent() {
        //authenticate
        String EXISTING_LOGIN = "lovanda";
        String EXISTING_PASSWORD = "hardPass";
        AccountCredentials accountCredentials =
                new AccountCredentials(EXISTING_LOGIN, EXISTING_PASSWORD);

        ResponseEntity<AccountCredentials> responseForAuth =
                testRestTemplate.postForEntity(
                        "/signin",
                        accountCredentials,
                        AccountCredentials.class
                );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", responseForAuth.getHeaders().getFirst("Authorization"));
        //given
        String url = "/users/{userId}/liked/{eventId}";

        Map<String, Long> urlParams = new HashMap<>();
        urlParams.put("userId", 4L);
        urlParams.put("eventId", 4L);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        //when
        ResponseEntity<?> response =
                testRestTemplate.exchange(
                        builder.buildAndExpand(urlParams).toUriString(),
                        HttpMethod.DELETE,
                        new HttpEntity<>(headers),
                        EventDTO.class
                );
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}