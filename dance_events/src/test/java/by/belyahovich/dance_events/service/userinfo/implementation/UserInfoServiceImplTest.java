package by.belyahovich.dance_events.service.userinfo.implementation;

import by.belyahovich.dance_events.config.ResourceNotFoundException;
import by.belyahovich.dance_events.domain.User;
import by.belyahovich.dance_events.domain.UserInfo;
import by.belyahovich.dance_events.repository.user.UserRepositoryJpa;
import by.belyahovich.dance_events.repository.userinfo.UserInfoRepository;
import by.belyahovich.dance_events.service.userinfo.UserInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("UserInfo service unit-testing")
class UserInfoServiceImplTest {
    private static final String EXIST_USER_1_LOGIN = "Vadim";
    private static final String EXIST_USER_1_PASSWORD = "$2y$10$4HBKgn/t6Un7SgEd6UOF4.sT0qNBTeWAwPEeHCSOlvD2tNRFVlU9G";
    private static final Long SOME_RANDOM_NUMBER = 1L;
    protected User user_1 = new User();
    protected UserInfo userInfo_1 = new UserInfo();
    private UserInfoService userInfoService;
    private UserInfoRepository userInfoRepository;
    private UserRepositoryJpa userRepositoryJpa;

    @BeforeEach
    public void init() {
        userInfoRepository = Mockito.mock(UserInfoRepository.class);
        userRepositoryJpa = Mockito.mock(UserRepositoryJpa.class);
        userInfoService = new UserInfoServiceImpl(userInfoRepository, userRepositoryJpa);

        user_1.setId(SOME_RANDOM_NUMBER);
        user_1.setLogin(EXIST_USER_1_LOGIN);
        user_1.setPassword(EXIST_USER_1_PASSWORD);
        user_1.setActive(true);

        userInfo_1.setUser(user_1);
        userInfo_1.setName("SOME_NAME");
        userInfo_1.setSurname("SOME_SURNAME");
    }

    @Test
    public void createUserInfo_withNotExistingUser_shouldThrowException() {
        //when
        when(userRepositoryJpa.findUserByLogin(anyString())).thenReturn(Optional.empty());
        //then
        assertThrows(ResourceNotFoundException.class,
                () -> userInfoService.createUserInfo(userInfo_1));
    }

    @Test
    public void deleteUserInfo_withExistingUser_shouldProperlyDeleteUserInformation() {
        //when
        when(userRepositoryJpa.findUserByLogin(anyString())).thenReturn(Optional.of(user_1));
        when(userInfoRepository.findById(anyLong())).thenReturn(Optional.of(userInfo_1));
        //then
        userInfoService.deleteUserInfo(user_1, userInfo_1);
        verify(userInfoRepository, times(1)).deleteById(userInfo_1.getId());
    }

    @Test
    public void deleteUserInfo_withNotExistingUser_shouldThrowException() {
        //when
        when(userRepositoryJpa.findUserByLogin(anyString())).thenReturn(Optional.empty());
        //then
        assertThrows(ResourceNotFoundException.class,
                () -> userInfoService.deleteUserInfo(user_1, userInfo_1));

        verify(userInfoRepository, times(0)).deleteById(userInfo_1.getId());
    }
}