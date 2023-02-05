package by.belyahovich.dance_events.repository.user;

import by.belyahovich.dance_events.domain.Event;
import by.belyahovich.dance_events.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("UserRepository module test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRepositoryJpa userRepositoryJpa;

    @Sql(scripts = {"/sql/clearDatabase.sql", "/sql/addRolesForUsers.sql"})
    @Test
    public void findAll_with4Entity_shouldProperlyFindAllUsers() {
        //given
        int EXPECTED_COUNT_OF_USERS_ON_DB = 4;
        //when
        Iterable<User> actualUsersFromDB = userRepository.findAll();
        //then
        assertThat(actualUsersFromDB).hasSize(EXPECTED_COUNT_OF_USERS_ON_DB);
    }

    @Sql(scripts = {"/sql/clearDatabase.sql"})
    @Test
    public void save_withNewUser_shouldProperlySaveNewUser() {
        //given
        User newUser = createNewUser();
        User expectedUserInDb = userRepository.save(newUser);
        //when
        Optional<User> actualUserFromDb = userRepository.findById(expectedUserInDb.getId());
        //then
        assertThat(actualUserFromDb.orElseThrow()).isEqualTo(expectedUserInDb);
    }

    @Sql(scripts = {"/sql/clearDatabase.sql", "/sql/addRolesForUsers.sql"})
    @Test
    public void findUserById_withExistingUser_shouldProperlyFindUser() {
        //given
        Long EXPECTED_USER_ID = 2L;
        //when
        User ACTUAL_USER = userRepository.findById(EXPECTED_USER_ID).orElseThrow();
        //then
        assertThat(ACTUAL_USER.getId()).isEqualTo(EXPECTED_USER_ID);
    }

    @Sql(scripts = {"/sql/clearDatabase.sql", "/sql/addRolesForUsers.sql"})
    @Test
    public void findUserByLogin_withExistingUser_shouldProperlyFindUser() {
        //given
        String ACTUAL_LOGIN = "Vilat";
        User newUser = createNewUser();
        User expectedUserToDB = userRepository.save(newUser);
        //when
        Optional<User> actualUserFromDB = userRepositoryJpa.findUserByLogin(ACTUAL_LOGIN);
        //then
        assertThat(actualUserFromDB).isPresent();
        assertThat(actualUserFromDB.get().hashCode()).isEqualTo(expectedUserToDB.hashCode());
    }

    @Sql(scripts = {"/sql/clearDatabase.sql", "/sql/addRolesForUsers.sql"})
    @Test
    public void findUserByLogin_withNotExistingUser_shouldReturnException() {
        //given
        String NOT_EXISTING_LOGIN = "Alena777";
        //when
        Optional<User> actualUserFromDB = userRepositoryJpa.findUserByLogin(NOT_EXISTING_LOGIN);
        //then
        assertThat(actualUserFromDB).isNotPresent();
    }

    @Sql(scripts = {"/sql/clearDatabase.sql", "/sql/addRolesForUsers.sql"})
    @Test
    public void deleteById_withExistingUser_shouldProperlyDeleteUserById() {
        //given
        int ALL_COUNT_OFF_USERS_AFTER_DELETE_ONE = 4;
        int ALL_COUNT_OFF_USERS_BEFORE_DELETE_ONE = 5;
        User newUser = createNewUser();
        User expectedUserToDb = userRepository.save(newUser);
        Iterable<User> allUsersFromDBBeforeDelete = userRepository.findAll();
        assertThat(allUsersFromDBBeforeDelete).hasSize(ALL_COUNT_OFF_USERS_BEFORE_DELETE_ONE);
        //when
        userRepository.deleteById(expectedUserToDb.getId());
        Iterable<User> allUsersFromDB = userRepository.findAll();
        //then
        assertThat(allUsersFromDB).hasSize(ALL_COUNT_OFF_USERS_AFTER_DELETE_ONE);
    }

    @Sql(scripts = {"/sql/clearDatabase.sql", "/sql/addRolesForUsers.sql"})
    @Test
    public void update_withNewUserInformation_shouldProperlyUpdateUserInformation() {
        //given
        String USER_LOGIN_FOR_UPDATE = "igor88";
        Optional<User> userToUpdate = userRepositoryJpa.findUserByLogin(USER_LOGIN_FOR_UPDATE);
        assertThat(userToUpdate).isPresent();
        //when
        userToUpdate.get().setActive(false);
        userRepository.save(userToUpdate.get());
        Optional<User> actualUserFromDB = userRepository.findById(userToUpdate.get().getId());
        //then
        assertThat(actualUserFromDB).isPresent();
        assertThat(actualUserFromDB.get().isActive()).isFalse();
    }

    @Sql(scripts = {"/sql/clearDatabase.sql", "/sql/addRolesForUsers.sql"})
    @Test
    public void findAllLikedUserEventsByLogin_withExistingUser_shouldProperlyFindAllUserLikedEvents() {
        //given
        Long USER_ID_FOR_UPDATE = 3L;
        String USER_LOGIN_FOR_UPDATE = "igor88";
        int EXPECTED_COUNT_OF_EVENT = 2;
        Optional<User> userForSearching = userRepositoryJpa.findUserByLogin(USER_LOGIN_FOR_UPDATE);
        assertThat(userForSearching).isPresent();
        //when
        List<Event> actualUserEvents = userRepositoryJpa.getAllLikedUserEventsByUserId(USER_ID_FOR_UPDATE);
        //then
        assertThat(actualUserEvents).hasSize(EXPECTED_COUNT_OF_EVENT);
    }

    @Sql(scripts = {"/sql/clearDatabase.sql", "/sql/addRolesForUsers.sql"})
    @Test
    @Rollback(value = false)
    public void updateUserActive_withExistingUser_shouldProperlyUpdateUserActive() {
        //given
        String USER_LOGIN_FOR_UPDATE = "sergey13";
        Optional<User> userByLogin = userRepositoryJpa.findUserByLogin(USER_LOGIN_FOR_UPDATE);
        assertThat(userByLogin).isPresent();
        assertThat(userByLogin.get().isActive()).isFalse();
        //when
        userRepositoryJpa.updateUserActive(USER_LOGIN_FOR_UPDATE, true);
        Optional<User> actualByLogin = userRepositoryJpa.findUserByLogin(USER_LOGIN_FOR_UPDATE);
        //then
        assertThat(actualByLogin).isPresent();
        assertThat(actualByLogin.get().isActive()).isTrue();
    }

    private User createNewUser() {
        User newUserForReturn = new User();
        newUserForReturn.setLogin("Vilat");
        newUserForReturn.setPassword("$2y$10$4HBKgn/t6Un7SgEd6UOF4.sT0qNBTeWAwPEeHCSOlvD2tNRFVlU9G");
        newUserForReturn.setActive(true);
        return newUserForReturn;
    }
}