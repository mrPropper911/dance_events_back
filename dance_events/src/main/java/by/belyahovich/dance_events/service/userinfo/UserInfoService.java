package by.belyahovich.dance_events.service.userinfo;

import by.belyahovich.dance_events.domain.User;
import by.belyahovich.dance_events.domain.UserInfo;

public interface UserInfoService {

    UserInfo createUserInfo (UserInfo userInfo);

    void deleteUserInfo (User user, UserInfo userInfo);

    UserInfo findUserInfoByUserLogin (String userLogin);
}
