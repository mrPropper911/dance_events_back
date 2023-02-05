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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final UserRepositoryJpa userRepositoryJpa;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final EventRepository eventRepository;
    private final EventDTOMapper eventDTOMapper;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserRepositoryJpa userRepositoryJpa,
                           EventRepository eventRepository,
                           EventDTOMapper eventDTOMapper,
                           RoleService roleService) {
        this.userRepository = userRepository;
        this.userRepositoryJpa = userRepositoryJpa;
        this.eventRepository = eventRepository;
        this.eventDTOMapper = eventDTOMapper;
        this.roleService = roleService;
    }

    @Override
    public List<User> allUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), true).toList();
    }

    @Override
    public Optional<User> findUserByLogin(String login) {
        return Optional.ofNullable(userRepositoryJpa.findUserByLogin(login).orElseThrow(
                () -> new ResourceNotFoundException("USER WITH LOGIN: " + login + " NOT EXISTS")
        ));
    }

    @Override
    public void findUserByLoginAndPassword(String login, String password) {
        User userByLogin = userRepositoryJpa.findUserByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("INCORRECT LOGIN OR PASSWORD"));
        if (userByLogin != null) {
            if (!bCryptPasswordEncoder.matches(password, userByLogin.getPassword())) {
                throw new ResourceNotFoundException("INCORRECT LOGIN OR PASSWORD");
            }
        }
    }

    @Override
    public void createUser(ProfileRequest request) {
        Optional<User> userByLogin = userRepositoryJpa.findUserByLogin(request.login());
        if (userByLogin.isPresent()) {
            throw new ResourceNotFoundException("THIS USER WITH LOGIN: " +
                    request.login() + " ALREADY EXISTS");
        }
        Role role = roleService.findRoleByTitle(request.roleTitle())
                .orElseThrow(() -> new ResourceNotFoundException("THIS ROLE: " +
                        request.roleTitle() + " DOES NOT EXIST"));
        User user = new User();
        user.setLogin(request.login());
        user.setPassword(bCryptPasswordEncoder.encode(request.password()));
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        User userToDelete = userRepositoryJpa.findUserByLogin(user.getLogin())
                .orElseThrow(() -> new ResourceNotFoundException("THIS USER WITH LOGIN: "
                        + user.getLogin() + " NOT EXISTS"));
        userRepository.deleteById(userToDelete.getId());
    }

    @Override
    public void updateUserActive(Long userId, boolean active) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("THIS USER WITH ID: " + userId + " NOT EXISTS"));
        user.setActive(active);
        userRepository.save(user);
    }

    @Override
    public List<EventDTO> getAllLikedUserEventsSortedByStartDate(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("THIS USER WITH ID: " + userId + " NOT FOUND"));
        return user.getLikedEvents()
                .stream()
                .map(eventDTOMapper)
                .toList()
                .stream().sorted(Comparator.comparing(EventDTO::startDate))
                .collect(Collectors.toList());
    }

    @Override
    public void addLikeEventToUser(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("THIS USER WITH ID: " + userId + " NOT FOUND"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("THIS EVENT WITH ID: " + eventId + " NOT FOUND"));
        Set<Event> likedEvents = user.getLikedEvents();
        likedEvents.add(event);

        userRepository.save(user);
    }

    @Override
    public void deleteLikedEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("THIS USER WITH ID: " + userId + " NOT FOUND"));
        if (!user.getLikedEvents().removeIf(e -> e.getId() == eventId)) {
            throw new ResourceNotFoundException("USER ID: " + userId + " DON'T HAVE EVENT ID: " + eventId);
        }
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = findUserByLogin(login);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("THIS USER WITH LOGIN: " + login + " NOT EXISTS");
        }
        return new org.springframework.security.core.userdetails.User(user.get().getLogin(), user.get().getPassword(),
                true, true, true, true, user.get().getAuthorities());
    }
}