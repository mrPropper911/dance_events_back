package by.belyahovich.dance_events.dto;

import by.belyahovich.dance_events.domain.UserInfo;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserInfoDTOMapper implements Function<UserInfo, UserInfoDTO> {

    @Override
    public UserInfoDTO apply(UserInfo userInfo) {
        return new UserInfoDTO(
                userInfo.getId(),
                userInfo.getName(),
                userInfo.getSurname(),
                userInfo.getPhone(),
                userInfo.getEmail()
        );
    }

    public UserInfo toUserInfo(UserInfoDTO userInfoDTO){
        return new UserInfo(
                userInfoDTO.id(),
                userInfoDTO.name(),
                userInfoDTO.surname(),
                userInfoDTO.phone(),
                userInfoDTO.name()
        );
    }
}
