package by.belyahovich.dance_events.service.user.implementation;

import by.belyahovich.dance_events.config.ResourceNotFoundException;
import by.belyahovich.dance_events.domain.Event;
import by.belyahovich.dance_events.domain.Role;
import by.belyahovich.dance_events.domain.User;
import by.belyahovich.dance_events.repository.role.RoleRepository;
import by.belyahovich.dance_events.repository.user.UserRepository;
import by.belyahovich.dance_events.repository.user.UserRepositoryJpa;
import by.belyahovich.dance_events.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final UserRepositoryJpa userRepositoryJpa;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRepositoryJpa userRepositoryJpa,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userRepositoryJpa = userRepositoryJpa;
        this.roleRepository = roleRepository;
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

    //todo test
    @Override
    public Optional<User> findUserByLoginAndPassword(String login, String password) {
        Optional<User> userByLogin = userRepositoryJpa.findUserByLogin(login);
        if (userByLogin.isPresent()){
            if (bCryptPasswordEncoder.matches(password, userByLogin.get().getPassword())){
                return userByLogin;
            }
        }
        return Optional.empty();
    }

    @Override
    public User createUser(User user) {
        Optional<User> userToSave = userRepositoryJpa.findUserByLogin(user.getLogin());
        if (userToSave.isPresent()) {
            throw new ResourceNotFoundException("THIS USER WITH LOGIN: " + user.getLogin() + " ALREADY EXISTS");
        }
        Optional<Role> roleUserToSave = roleRepository.findById(user.getRole().getId());
        if (roleUserToSave.isEmpty()) {
            throw new ResourceNotFoundException("THIS ROLE: " + user.getRole().getRoleTitle() + " DOES NOT EXIST, FIRST CREATE IT");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        Optional<User> userToDelete = userRepositoryJpa.findUserByLogin(user.getLogin());
        if (userToDelete.isEmpty()) {
            throw new ResourceNotFoundException("THIS USER WITH LOGIN: " + user.getLogin() + " NOT EXISTS");
        }
        userRepository.deleteById(userToDelete.get().getId());
    }

    @Override
    public List<Event> getAllLikedUserEventsByUser(User user) {
        Optional<User> userToSearch = userRepositoryJpa.findUserByLogin(user.getLogin());
        if (userToSearch.isEmpty()) {
            throw new ResourceNotFoundException("THIS USER WITH LOGIN: " + user.getLogin() + " NOT EXISTS");
        }
        return userRepositoryJpa.getAllLikedUserEventsByUserLogin(user.getLogin());
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