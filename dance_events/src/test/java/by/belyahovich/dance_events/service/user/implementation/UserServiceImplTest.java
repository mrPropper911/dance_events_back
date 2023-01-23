package by.belyahovich.dance_events.service.user.implementation;

import by.belyahovich.dance_events.config.ResourceNotFoundException;
import by.belyahovich.dance_events.domain.Event;
import by.belyahovich.dance_events.domain.EventType;
import by.belyahovich.dance_events.domain.Role;
import by.belyahovich.dance_events.domain.User;
import by.belyahovich.dance_events.repository.role.RoleRepository;
import by.belyahovich.dance_events.repository.user.UserRepository;
import by.belyahovich.dance_events.repository.user.UserRepositoryJpa;
import by.belyahovich.dance_events.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("UserService unit-testing")
class UserServiceImplTest {
    private static final String EXIST_USER_1_LOGIN = "Vadim";
    private static final String EXIST_USER_1_PASSWORD = "$2y$10$4HBKgn/t6Un7SgEd6UOF4.sT0qNBTeWAwPEeHCSOlvD2tNRFVlU9G";

    protected User user_1 = new User();
    protected User user_2 = new User();
    protected Role role = new Role();
    protected Event event_1 = new Event();
    protected Event event_2 = new Event();

    private UserRepository userRepository;
    private UserRepositoryJpa userRepositoryJpa;
    private RoleRepository roleRepository;
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void init() {
        userRepository = Mockito.mock(UserRepository.class);
        userRepositoryJpa = Mockito.mock(UserRepositoryJpa.class);
        roleRepository = Mockito.mock(RoleRepository.class);
        bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        userService = new UserServiceImpl(userRepository, userRepositoryJpa, roleRepository);

        user_1.setLogin(EXIST_USER_1_LOGIN);
        user_1.setPassword(EXIST_USER_1_PASSWORD);
        user_1.setActive(true);
        user_2.setLogin("Loki");
        user_2.setPassword("$2y$10$4HBKgn/t6Un7SgEd6UOF4.sT0qNBTeWAS@)F@FS($#GG2tNRFVlU9G");
        user_2.setActive(false);

        role.setId(1L);
        role.setRoleTitle("Omega");
        user_1.setRole(role);
        user_2.setRole(role);

        EventType eventType = new EventType();
        eventType.setId(1L);
        eventType.setType("SOME_TYPE");
        event_1.setTitle("SOME_NAME_1");
        event_2.setTitle("SOME_NAME_2");
        event_1.setEventType(eventType);
        event_2.setEventType(eventType);
    }

    @Test
    public void allUsers_with2Entity_shouldProperlyFindAllUsers() {
        //given
        List<User> expectedUserList = Arrays.asList(user_1, user_2);
        //when
        when(userRepository.findAll()).thenReturn(expectedUserList);
        List<User> actualUserList = userService.allUsers();
        //then
        assertThat(actualUserList).hasSize(expectedUserList.size());
    }

    @Test
    public void findUserByLogin_withExistingUser_shouldProperlyFindUser() {
        //when
        when(userRepositoryJpa.findUserByLogin(anyString())).thenReturn(Optional.of(user_1));
        Optional<User> actualUser = userService.findUserByLogin(EXIST_USER_1_LOGIN);
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
    public void createUser_withExistingUser_shouldThrowException() {
        //when
        when(userRepositoryJpa.findUserByLogin(anyString())).thenReturn(Optional.of(user_1));
        //then
        assertThrows(ResourceNotFoundException.class,
                () -> userService.createUser(user_1));
    }

    @Test
    public void createUser_withNotExistingUser_shouldProperlyCreateNewUser() {
        //given
        when(userRepositoryJpa.findUserByLogin(anyString())).thenReturn(Optional.empty());
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(EXIST_USER_1_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(user_1);
        //when
        User actualUser = userService.createUser(user_1);
        //then
        assertThat(actualUser).isNotNull();
        assertThat(actualUser).isEqualTo(user_1);
        verify(userRepository, times(1)).save(user_1);
    }

    @Test
    public void deleteUser_withExistingUser_shouldProperlyDeleteUser() {
        //given
        when(userRepositoryJpa.findUserByLogin(anyString())).thenReturn(Optional.of(user_1));
        //when
        userService.deleteUser(user_1);
        //then
        verify(userRepository, times(1)).deleteById(user_1.getId());
    }

    @Test
    public void deleteUser_withNotExistingUser_shouldThrowException() {
        //given
        when(userRepositoryJpa.findUserByLogin(anyString())).thenReturn(Optional.empty());
        //when
        assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(user_1));
        //then
        verify(userRepository, times(0)).deleteById(user_1.getId());
    }

    @Test
    public void getAllLikedUserEventsByUser_withExistingEvents_shouldProperlyReturnAllEventsForUser() {
        //given
        List<Event> EVENTS_LIKED_USER = List.of(event_1, event_2);
        when(userRepositoryJpa.findUserByLogin(anyString())).thenReturn(Optional.of(user_1));
        when(userRepositoryJpa.getAllLikedUserEventsByUserLogin(anyString())).thenReturn(EVENTS_LIKED_USER);
        //when
        List<Event> actualUserLikedEvents = userService.getAllLikedUserEventsByUser(user_1);
        //then
        assertThat(actualUserLikedEvents).hasSize(EVENTS_LIKED_USER.size());
        assertThat(actualUserLikedEvents).isEqualTo(EVENTS_LIKED_USER);
        verify(userRepositoryJpa, times(1)).getAllLikedUserEventsByUserLogin(user_1.getLogin());
    }
}