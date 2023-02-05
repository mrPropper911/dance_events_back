package by.belyahovich.dance_events.service.user.implementation;

import by.belyahovich.dance_events.config.ResourceNotFoundException;
import by.belyahovich.dance_events.controller.authorization.ProfileRequest;
import by.belyahovich.dance_events.domain.*;
import by.belyahovich.dance_events.dto.EventDTO;
import by.belyahovich.dance_events.dto.EventDTOMapper;
import by.belyahovich.dance_events.repository.event.EventRepository;
import by.belyahovich.dance_events.repository.user.UserRepository;
import by.belyahovich.dance_events.repository.user.UserRepositoryJpa;
import by.belyahovich.dance_events.service.role.RoleService;
import by.belyahovich.dance_events.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE_TIME;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("UserService unit-testing")
class UserServiceImplTest {

    private UserService userService;

    private UserRepository userRepository;
    private UserRepositoryJpa userRepositoryJpa;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private EventDTOMapper eventDTOMapper;
    private EventRepository eventRepository;
    private RoleService roleService;


    @BeforeEach
    public void init() {
        userRepository = Mockito.mock(UserRepository.class);
        userRepositoryJpa = Mockito.mock(UserRepositoryJpa.class);
        bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        eventRepository = Mockito.mock(EventRepository.class);
        eventDTOMapper = Mockito.mock(EventDTOMapper.class);
        roleService = Mockito.mock(RoleService.class);
        userService = new UserServiceImpl(
                userRepository,
                userRepositoryJpa,
                eventRepository,
                eventDTOMapper,
                roleService
        );
    }

    @Test
    public void allUsers_with2Entity_shouldProperlyFindAllUsers() {
        //given
        User user_1 = new User(
                1L,
                "SOME_LOGIN",
                "SOME_PASSWORD",
                true,
                new Role(1L, "ROLE_ADMIN")
        );
        User user_2 = new User(
                1L,
                "SOME_LOGIN",
                "SOME_PASSWORD",
                true,
                new Role(2L, "ROLE_ADMIN")
        );
        List<User> expectedUserList = Arrays.asList(user_1, user_2);
        //when
        when(userRepository.findAll()).thenReturn(expectedUserList);
        List<User> actualUserList = userService.allUsers();
        //then
        assertThat(actualUserList).hasSize(expectedUserList.size());
    }

    @Test
    public void findUserByLogin_withExistingUser_shouldProperlyFindUser() {
        //given
        User user_1 = new User(
                1L,
                "SOME_LOGIN",
                "SOME_PASSWORD",
                true,
                new Role(1L, "ROLE_ADMIN")
        );
        //when
        when(userRepositoryJpa.findUserByLogin(anyString())).thenReturn(Optional.of(user_1));
        Optional<User> actualUser = userService.findUserByLogin("SOME_LOGIN");
        //then
        assertThat(actualUser).isPresent();
        assertThat(actualUser).isEqualTo(Optional.of(user_1));
    }

    @Test
    public void findUserByLogin_withNotExistingUser_shouldThrowResourceNotFoundException() {
        //when
        when(userRepositoryJpa.findUserByLogin(anyString())).thenReturn(Optional.empty());
        //then
        assertThrows(ResourceNotFoundException.class,
                () -> userService.findUserByLogin("SOME_RANDOM_NOT_EXIST_LOGIN"));
    }

    @Test
    public void findUserByLoginAndPassword_withExistingUser_shouldContinue() {
        //given
        User user_1 = new User(
                1L,
                "user123",
                "$2a$10$4RCA6v9iVByB3C0.EGDBfOR64WqxSfLiin/m4z8Oqlr5nIaUpmnhi",
                true,
                new Role(1L, "ROLE_ADMIN")
        );
        //when
        when(userRepositoryJpa.findUserByLogin(anyString())).thenReturn(Optional.of(user_1));
        when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(true);
        userService.findUserByLoginAndPassword("user123", "user123");
        //then
        verify(userRepositoryJpa, times(1))
                .findUserByLogin("user123");
    }

    @Test
    public void findUserByLoginAndPassword_withNotExistingUser_shouldThrowException() {
        //when
        when(userRepositoryJpa.findUserByLogin(anyString())).thenReturn(Optional.empty());
        //then
        assertThrows(ResourceNotFoundException.class,
                () -> userService.findUserByLoginAndPassword("SOME_LOGIN", "SOME_PASSWORD"));
    }

    @Test
    public void findUserByLoginAndPassword_withNotCorrectPassword_shouldThrowException() {
        //given
        User user_1 = new User(
                1L,
                "SOME_LOGIN",
                "SOME_PASSWORD",
                true,
                new Role(1L, "ROLE_ADMIN")
        );
        //when
        when(userRepositoryJpa.findUserByLogin(anyString())).thenReturn(Optional.of(user_1));
        when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(false);
        //then
        assertThrows(ResourceNotFoundException.class,
                () -> userService.findUserByLoginAndPassword("SOME_LOGIN", "SOME_PASSWORD"));
    }

    @Test
    public void createUser_withExistingUser_shouldThrowException() {
        //given
        User user_1 = new User(
                1L,
                "SOME_LOGIN",
                "SOME_PASSWORD",
                true,
                new Role(1L, "ROLE_ADMIN")
        );
        ProfileRequest profileRequest = new ProfileRequest(
                "SOME_LOGIN",
                "SOME_PASSWORD",
                "SOME_TITLE_ROLE"
        );
        //when
        when(userRepositoryJpa.findUserByLogin(anyString())).thenReturn(Optional.of(user_1));
        //then
        assertThrows(ResourceNotFoundException.class,
                () -> userService.createUser(profileRequest));
    }

    @Test
    public void createUser_withNotExistingRole_shouldThrowException() {
        //given
        User user_1 = new User(
                1L,
                "SOME_LOGIN",
                "SOME_PASSWORD",
                true,
                new Role(1L, "ROLE_ADMIN")
        );
        ProfileRequest profileRequest = new ProfileRequest(
                "SOME_LOGIN",
                "SOME_PASSWORD",
                "SOME_TITLE_ROLE"
        );
        //when
        when(userRepositoryJpa.findUserByLogin(anyString())).thenReturn(Optional.empty());
        when(roleService.findRoleByTitle(anyString())).thenReturn(Optional.empty());
        //then
        assertThrows(ResourceNotFoundException.class,
                () -> userService.createUser(profileRequest));
    }

    @Test
    public void createUser_withNotExistingUser_shouldProperlyCreateNewUser() {
        //given
        ProfileRequest profileRequest = new ProfileRequest(
                "SOME_LOGIN",
                "SOME_PASSWORD",
                "SOME_TITLE_ROLE"
        );
        Role role = new Role(1L, "ROLE_TEST");
        //when
        when(userRepositoryJpa.findUserByLogin(anyString())).thenReturn(Optional.empty());
        when(roleService.findRoleByTitle(anyString())).thenReturn(Optional.of(role));
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("EXIST_USER_1_PASSWORD");
        userService.createUser(profileRequest);
        //then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void deleteUser_withExistingUser_shouldProperlyDeleteUser() {
        //given
        User user_1 = new User(
                1L,
                "SOME_LOGIN",
                "SOME_PASSWORD",
                true,
                new Role(1L, "ROLE_ADMIN")
        );
        //when
        when(userRepositoryJpa.findUserByLogin(anyString())).thenReturn(Optional.of(user_1));
        userService.deleteUser(user_1);
        //then
        verify(userRepository, times(1)).deleteById(user_1.getId());
    }

    @Test
    public void deleteUser_withNotExistingUser_shouldThrowException() {
        //given
        User user_1 = new User(
                1L,
                "SOME_LOGIN",
                "SOME_PASSWORD",
                true,
                new Role(1L, "ROLE_ADMIN")
        );
        //when
        when(userRepositoryJpa.findUserByLogin(anyString())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(user_1));
        //then
        verify(userRepository, times(0)).deleteById(user_1.getId());
    }

    @Test
    public void updateUserActive_withExistingUser_shouldProperlyUpdateActiveField() {
        //given
        User user_1 = new User(
                1L,
                "SOME_LOGIN",
                "SOME_PASSWORD",
                true,
                new Role(1L, "ROLE_ADMIN")
        );
        //when
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user_1));
        userService.updateUserActive(1L, false);
        //then
        verify(userRepository, times(1)).save(user_1);
    }

    @Test
    public void updateUserActive_withNotExistingUser_shouldProperlyUpdateActiveField() {
        //when
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        //then
        assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUserActive(1L, false));
    }

//    @Test
//    public void getAllLikedUserEventsByUser_withExistingEvents_shouldProperlyReturnAllEventsForUser() {
//        //given
//        Event event_1 = new Event(
//                "SOME_TITLE",
//                new Date(System.currentTimeMillis() + 1_000),
//                new Date(System.currentTimeMillis() + 3_000),
//                "SOME_DESC",
//                true,
//                new EventType(1L, "SOME_TYPE")
//        );
//        Event event_2 = new Event(
//                "SOME_TITLE",
//                new Date(System.currentTimeMillis() + 10_000),
//                new Date(System.currentTimeMillis() + 20_000),
//                "SOME_DESC",
//                true,
//                new EventType(1L, "SOME_TYPE")
//        );
//        User user_1 = new User(
//                1L,
//                "SOME_LOGIN",
//                "SOME_PASSWORD",
//                true,
//                new Role(1L, "ROLE_ADMIN")
//        );
//        Set<Event> EVENTS_LIKED_USER = Set.of(event_1, event_2);
//        user_1.setLikedEvents(EVENTS_LIKED_USER);
//        user_1.setUserInfo(new UserInfo());
//        EventDTO eventDTO = eventDTOMapper.apply(event_1);
//        //when
//        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user_1));
//        when(eventDTOMapper.apply(any(Event.class))).thenReturn(eventDTO);
//        when(userService.getAllLikedUserEventsSortedByStartDate(anyLong())).thenReturn( List.of(eventDTO));
//        List<EventDTO> actualUserLikedEvents = userService.getAllLikedUserEventsSortedByStartDate(anyLong());
//        //then
//        assertThat(actualUserLikedEvents).hasSize(EVENTS_LIKED_USER.size());
//        verify(userRepository, times(1)).findById(anyLong());
//    }


}