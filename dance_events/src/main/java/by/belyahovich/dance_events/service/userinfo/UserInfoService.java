package by.belyahovich.dance_events.service.userinfo;

import by.belyahovich.dance_events.domain.User;
import by.belyahovich.dance_events.domain.UserInfo;
import by.belyahovich.dance_events.dto.UserInfoDTO;

public interface UserInfoService {

    void saveUserInfo (Long userId, UserInfoDTO userInfoDTO);

    void deleteUserInfo (UserInfo userInfo);

    UserInfoDTO findUserInfoByUserId (Long userId);
}
