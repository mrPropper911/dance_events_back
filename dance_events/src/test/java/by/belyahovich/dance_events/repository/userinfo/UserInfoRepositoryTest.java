package by.belyahovich.dance_events.repository.userinfo;

import by.belyahovich.dance_events.domain.User;
import by.belyahovich.dance_events.domain.UserInfo;
import by.belyahovich.dance_events.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserInfoRepository module test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserInfoRepositoryTest {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Sql(scripts = {"/sql/clearDatabase.sql", "/sql/addRolesForUsers.sql"})
    @Test
    public void findUserInfoByUserId_withExistingUser_shouldProperlyFindUserInfoByUserId() {
        //given
        String EXPECTED_USER_NAME = "Vadia";
        //when
        Optional<UserInfo> actualUserInfo = userInfoRepository.findById(1L);
        //then
        assertThat(actualUserInfo).isPresent();
        assertThat(actualUserInfo.get().getName()).isEqualTo(EXPECTED_USER_NAME);
    }

    @Sql(scripts = {"/sql/clearDatabase.sql"})
    @Test
    public void saveUserInformation_withNotExistingUser_shouldProperlySaveUserInformation() {
        //given
        String NAME_NEW_USER = "Mark";
        User newUser = new User();
        newUser.setLogin("marko");
        newUser.setPassword("$2y$10$4HBKgn/t6Un7SgEd6UOF4.sT0qNBTeWAwPEeHCSOlvD2tNRFVlU9G");
        User savedUserToDB = userRepository.save(newUser);
        assertThat(savedUserToDB).isNotNull();

        UserInfo newUserInfo = new UserInfo();
        newUserInfo.setUser(savedUserToDB);
        newUserInfo.setName(NAME_NEW_USER);
        //when
        userInfoRepository.save(newUserInfo);
        Optional<UserInfo> actualUserInfoFromDB = userInfoRepository.findById(savedUserToDB.getId());
        //then
        assertThat(actualUserInfoFromDB).isPresent();
        assertThat(actualUserInfoFromDB.get().getName()).isEqualTo(NAME_NEW_USER);
    }
}