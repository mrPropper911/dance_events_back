package by.belyahovich.dance_events.service.user.implementation;

import by.belyahovich.dance_events.config.ResourceNotFoundException;
import by.belyahovich.dance_events.controller.authorization.ProfileRequest;
import by.belyahovich.dance_events.domain.Event;
import by.belyahovich.dance_events.domain.Role;
import by.belyahovich.dance_events.domain.User;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("UserService unit-test")
class UserServiceImplTest {

    private UserService userService;

    private UserServiceImpl userServiceImlToCheckUserDetails;

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

        userServiceImlToCheckUserDetails = new UserServiceImpl(
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
        verify(userRepository, never()).save(any());
    }

    @Test
    public void createUser_withNotExistingRole_shouldThrowException() {
        //given
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
        verify(userRepository, never()).save(any());
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

    @Test
    public void addLikeEventToUser_withNotExistingUser_shouldThrowException() {
        //when
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> userService.addLikeEventToUser(1L, 1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("THIS USER WITH ID: 1 NOT FOUND");
        verify(userRepository, never()).save(any());
    }

    @Test
    public void addLikeEventToUser_withNotExistingEvent_shouldThrowException() {
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
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> userService.addLikeEventToUser(1L, 1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("THIS EVENT WITH ID: 1 NOT FOUND");
        verify(userRepository, never()).save(any());
    }

    @Test
    public void addLikeEventToUser_withExistingEventAndUser_shouldProperlyAddLikeEventToUser() {
        //given
        Event event_1 = new Event(
                1L,
                "Title",
                new Date(System.currentTimeMillis() + 10_000),
                new Date(System.currentTimeMillis() + 90_000),
                "Description"
        );
        Event event_2 = new Event(
                2L,
                "Title2",
                new Date(System.currentTimeMillis() + 30_000),
                new Date(System.currentTimeMillis() + 110_000),
                "Description2"
        );
        User user_1 = new User(
                1L,
                "SOME_LOGIN",
                "SOME_PASSWORD",
                true,
                new Role(1L, "ROLE_ADMIN")
        );
        user_1.setLikedEvents(Set.of(event_2));
        //when
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user_1));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event_1));
        userService.addLikeEventToUser(user_1.getId(), event_1.getId());
        //then
        ArgumentCaptor<User> userArgumentCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user_1);
    }

    @Test
    public void deleteLikedEvent_withNotExistingUser_shouldThrowException() {
        //when
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        //
        assertThatThrownBy(() -> userService.deleteLikedEvent(1L, anyLong()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("THIS USER WITH ID: 1 NOT FOUND");
        verify(userRepository, never()).save(any());
    }

    @Test
    public void deleteLikedEvent_withNotExistingEventInUser_shouldThrowException() {
        //given
        User user_1 = new User(
                1L,
                "SOME_LOGIN",
                "SOME_PASSWORD",
                true,
                new Role(1L, "ROLE_ADMIN")
        );
        Event event_1 = new Event(
                1L,
                "Title",
                new Date(System.currentTimeMillis() + 10_000),
                new Date(System.currentTimeMillis() + 90_000),
                "Description"
        );
        user_1.setLikedEvents(Set.of(event_1));
        //when
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user_1));
        //
        assertThatThrownBy(() -> userService.deleteLikedEvent(1L, 2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("USER ID: 1 DON'T HAVE EVENT ID: 2");
        verify(userRepository, never()).save(any());
    }

    @Test
    public void deleteLikedEvent_withExistingEventInUser_shouldProperlyDelete() {
        //given
        User user_1 = new User(
                1L,
                "SOME_LOGIN",
                "SOME_PASSWORD",
                true,
                new Role(1L, "ROLE_ADMIN")
        );
        Event event_1 = new Event(
                1L,
                "Title",
                new Date(System.currentTimeMillis() + 10_000),
                new Date(System.currentTimeMillis() + 90_000),
                "Description"
        );
        user_1.setLikedEvents(Set.of(event_1));
        //when
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user_1));
        userService.deleteLikedEvent(1L, 1L);
        //then
        ArgumentCaptor<User> userArgumentCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user_1);
    }

    @Test
    public void getAllLikedUserEventsSortedByStartDate_withNotExistingUser_shouldThrowException() {
        //when
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> userService.getAllLikedUserEventsSortedByStartDate(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("THIS USER WITH ID: 1 NOT FOUND");
        verify(eventDTOMapper, never()).apply(any());
    }

    @Test
    public void getAllLikedUserEventsSortedByStartDate_withExistingEvents_shouldProperlyReturnAllEventsForUser() {
        //given
        Event event_1 = new Event(
                1L,
                "SOME_TITLE1",
                new Date(System.currentTimeMillis() + 100_000),
                new Date(System.currentTimeMillis() + 300_000),
                "SOME_DESC2"
        );
        User user_1 = new User(
                1L,
                "SOME_LOGIN",
                "SOME_PASSWORD",
                true,
                new Role(1L, "ROLE_ADMIN")
        );
        Set<Event> EVENTS_LIKED_USER = Set.of(event_1);
        user_1.setLikedEvents(EVENTS_LIKED_USER);
        EventDTO eventDTO = new EventDTO(
                1L,
                "Title",
                "Desc",
                new Date(),
                new Date(),
                "Event_Tupe",
                true
        );
        //when
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user_1));
        when(eventDTOMapper.apply(any(Event.class))).thenReturn(eventDTO);
        Set<EventDTO> actualUserLikedEvents = userService.getAllLikedUserEventsSortedByStartDate(1L);
        //then
        assertThat(actualUserLikedEvents).hasSize(EVENTS_LIKED_USER.size());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    public void loadUserByUsername_withNotExistingUser_shouldThrowException() {
        //when
        when(userRepositoryJpa.findUserByLogin(anyString())).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> userServiceImlToCheckUserDetails.loadUserByUsername("ANY_LOGIN"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("THIS USER WITH LOGIN: ANY_LOGIN NOT EXISTS");
    }

    @Test
    public void loadUserByUsername_withExistingUser_shouldProperlyReturn() {
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
        UserDetails userDetails = userServiceImlToCheckUserDetails.loadUserByUsername("SOME_LOGIN");
        //then
        assertThat(userDetails.getAuthorities()).hasSize(1);
        verify(userRepositoryJpa).findUserByLogin(anyString());
    }


}