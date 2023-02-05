package by.belyahovich.dance_events.service.userinfo.implementation;

import by.belyahovich.dance_events.config.ResourceNotFoundException;
import by.belyahovich.dance_events.domain.Role;
import by.belyahovich.dance_events.domain.User;
import by.belyahovich.dance_events.domain.UserInfo;
import by.belyahovich.dance_events.dto.UserInfoDTO;
import by.belyahovich.dance_events.dto.UserInfoDTOMapper;
import by.belyahovich.dance_events.repository.user.UserRepository;
import by.belyahovich.dance_events.repository.userinfo.UserInfoRepository;
import by.belyahovich.dance_events.service.userinfo.UserInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("UserInfoService unit-testing")
class UserInfoServiceImplTest {

    private UserInfoService userInfoService;

    private UserInfoRepository userInfoRepository;
    private UserRepository userRepository;
    private UserInfoDTOMapper userInfoDTOMapper;

    @BeforeEach
    public void init() {
        userInfoRepository = Mockito.mock(UserInfoRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        userInfoDTOMapper = Mockito.mock(UserInfoDTOMapper.class);
        userInfoService = new UserInfoServiceImpl(
                userInfoRepository,
                userInfoDTOMapper,
                userRepository
        );
    }

    @Test
    public void saveUserInfo_withNotExistingUser_shouldThrowException() {
        //given
        Optional<User> NOT_EXISTING_USER = Optional.empty();
        UserInfoDTO userInfoDTO = new UserInfoDTO(
                1L,
                "SOME_NAME",
                "SOME_SURNAME",
                "SOME_PHONE",
                "SOME_EMAIL");
        //when
        when(userRepository.findById(anyLong())).thenReturn(NOT_EXISTING_USER);
        //then
        assertThrows(ResourceNotFoundException.class,
                () -> userInfoService.saveUserInfo(1L, userInfoDTO));
    }

    @Test
    public void saveUserInfo_withNotEqualsUserIdAndUserInfoId_shouldThrowException() {
        //given
        UserInfoDTO userInfoDTO = new UserInfoDTO(
                1L,
                "SOME_NAME",
                "SOME_SURNAME",
                "SOME_PHONE",
                "SOME_EMAIL");
        //then
        assertThrows(IllegalArgumentException.class,
                () -> userInfoService.saveUserInfo(2L, userInfoDTO));
    }

    @Test
    public void saveUserInfo_withExistingUser_shouldProperlySaveUserInformation() {
        //given
        User EXISTING_USER = new User(
                1L,
                "SOME_LOGIN",
                "SOME_PASSWORD",
                true,
                new Role(1L, "ROLE_EXAMPLE")
        );
        Optional<User> OPTIONAL_EXISTING_USER = Optional.of(EXISTING_USER);
        UserInfoDTO userInfoDTO = new UserInfoDTO(
                1L,
                "SOME_NAME",
                "SOME_SURNAME",
                "SOME_PHONE",
                "SOME_EMAIL");
        //when
        when(userRepository.findById(anyLong())).thenReturn(OPTIONAL_EXISTING_USER);
        userInfoService.saveUserInfo(EXISTING_USER.getId(), userInfoDTO);
        //then
        verify(userInfoRepository, times(1))
                .save(userInfoDTOMapper.toUserInfo(userInfoDTO));
    }

    @Test
    public void findUserInfoByUserId_withExistingUser_shouldProperlyFindUserInfo() {
        //given
        UserInfo EXISTING_USER_INFO = new UserInfo(
                1L,
                "SOME_NAME",
                "SOME_SURNAME",
                "SOME_PHONE",
                "SOME_EMAIL"
        );
        Optional<UserInfo> OPTIONAL_EXISTING_USER_INFO =
                Optional.of(EXISTING_USER_INFO);
        UserInfoDTO EXPECTED_USER_INFO_DTO = new UserInfoDTO(
                1L,
                "SOME_NAME",
                "SOME_SURNAME",
                "SOME_PHONE",
                "SOME_EMAIL"
        );
        //when
        when(userInfoRepository.findById(anyLong())).thenReturn(OPTIONAL_EXISTING_USER_INFO);
        when(userInfoDTOMapper.apply(any(UserInfo.class))).thenReturn(EXPECTED_USER_INFO_DTO);
        UserInfoDTO ACTUAL_USER_INFO_DTO = userInfoService.findUserInfoByUserId(1L);
        //then
        assertThat(ACTUAL_USER_INFO_DTO).isNotNull();
        assertThat(ACTUAL_USER_INFO_DTO).isEqualTo(EXPECTED_USER_INFO_DTO);
    }

    @Test
    public void deleteUserInfo_withExistingUser_shouldProperlyDeleteUserInformation() {
        //given
        UserInfo EXISTING_USER_INFO = new UserInfo(
                1L,
                "SOME_NAME",
                "SOME_SURNAME",
                "SOME_PHONE",
                "SOME_EMAIL"
        );
        Optional<UserInfo> OPTIONAL_EXISTING_USER_INFO =
                Optional.of(EXISTING_USER_INFO);
        //when
        when(userInfoRepository.findById(anyLong())).thenReturn(OPTIONAL_EXISTING_USER_INFO);
        userInfoService.deleteUserInfo(EXISTING_USER_INFO);
        //then
        verify(userInfoRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteUserInfo_withNotExistingUser_shouldThrowException() {
        //given
        UserInfo EXISTING_USER_INFO = new UserInfo(
                1L,
                "SOME_NAME",
                "SOME_SURNAME",
                "SOME_PHONE",
                "SOME_EMAIL"
        );
        Optional<UserInfo> NOT_EXISTING_USER_INFO = Optional.empty();
        //when
        when(userInfoRepository.findById(anyLong())).thenReturn(NOT_EXISTING_USER_INFO);
        //then
        assertThrows(ResourceNotFoundException.class,
                () -> userInfoService.deleteUserInfo(EXISTING_USER_INFO));
    }
}