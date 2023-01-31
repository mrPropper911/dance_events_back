package by.belyahovich.dance_events.service.userinfo.implementation;

import by.belyahovich.dance_events.config.ResourceNotFoundException;
import by.belyahovich.dance_events.domain.User;
import by.belyahovich.dance_events.domain.UserInfo;
import by.belyahovich.dance_events.repository.user.UserRepositoryJpa;
import by.belyahovich.dance_events.repository.userinfo.UserInfoRepository;
import by.belyahovich.dance_events.service.userinfo.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final UserRepositoryJpa userRepositoryJpa;

    @Autowired
    public UserInfoServiceImpl(UserInfoRepository userInfoRepository, UserRepositoryJpa userRepositoryJpa) {
        this.userInfoRepository = userInfoRepository;
        this.userRepositoryJpa = userRepositoryJpa;
    }

    @Override
    public UserInfo createUserInfo(UserInfo userInfo) {
        Optional<User> userByLogin = userRepositoryJpa.findById(userInfo.getId());
        if (userByLogin.isEmpty()) {
            throw new ResourceNotFoundException("CAN'T CREATE/UPDATE USER INFORMATION, BECAUSE THIS USER: " +
                    userInfo.getUser().getLogin() + " NOT EXISTING");
        }
        return userInfoRepository.save(userInfo);
    }

    @Override
    public void deleteUserInfo(User user, UserInfo userInfo) {
        Optional<User> userByLogin = userRepositoryJpa.findUserByLogin(user.getLogin());
        if (userByLogin.isEmpty()) {
            throw new ResourceNotFoundException("CAN'T DELETE USER INFORMATION, BECAUSE THIS USER: " +
                    user.getLogin() + " NOT EXISTING");
        }
        Optional<UserInfo> userInformation = userInfoRepository.findById(userInfo.getId());
        if (userInformation.isEmpty()) {
            throw new ResourceNotFoundException("CAN'T DELETE USER INFORMATION, BECAUSE IS EMPTY");
        }
        userInfoRepository.deleteById(userInfo.getId());
    }

    @Override
    public UserInfo findUserInfoByUserLogin(String userLogin) {
        Long idUser = userRepositoryJpa.findUserByLogin(userLogin).orElseThrow().getId();
        UserInfo userInfo = userInfoRepository.findById(idUser)
                .orElseThrow(() -> new ResourceNotFoundException("CAN'T FIND USER INFORMATION, BECAUSE IS EMPTY"));
        return new UserInfo(userInfo.getId(), userInfo.getName(), userInfo.getSurname(),
                userInfo.getPhone(), userInfo.getEmail());
    }
}
