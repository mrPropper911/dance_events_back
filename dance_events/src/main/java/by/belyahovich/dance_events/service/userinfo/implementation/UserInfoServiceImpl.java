package by.belyahovich.dance_events.service.userinfo.implementation;

import by.belyahovich.dance_events.config.ResourceNotFoundException;
import by.belyahovich.dance_events.domain.UserInfo;
import by.belyahovich.dance_events.dto.UserInfoDTO;
import by.belyahovich.dance_events.dto.UserInfoDTOMapper;
import by.belyahovich.dance_events.repository.user.UserRepository;
import by.belyahovich.dance_events.repository.userinfo.UserInfoRepository;
import by.belyahovich.dance_events.service.userinfo.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final UserInfoDTOMapper userInfoDTOMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserInfoServiceImpl(
            UserInfoRepository userInfoRepository,
            UserInfoDTOMapper userInfoDTOMapper,
            UserRepository userRepository) {
        this.userInfoRepository = userInfoRepository;
        this.userInfoDTOMapper = userInfoDTOMapper;
        this.userRepository = userRepository;
    }

    @Override
    public void saveUserInfo(Long userId, UserInfoDTO userInfoDTO) {
        if (!Objects.equals(userId, userInfoDTO.id())) {
            throw new IllegalArgumentException("YOU CANT UPDATE NOT YOUR INFO");
        }
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("USER WITH ID: " +
                        userId + " NOT EXIST"));
        userInfoRepository.save(userInfoDTOMapper.toUserInfo(userInfoDTO));
    }

    @Override
    public UserInfoDTO findUserInfoByUserId(Long userId) {
        UserInfo userInfo = userInfoRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("CAN'T FIND USER INFORMATION ID: "
                        + userId + " NOT EXIST"));
        return userInfoDTOMapper.apply(userInfo);

    }

    @Override
    public void deleteUserInfo(UserInfo userInfo) {
        userInfoRepository.findById(userInfo.getId())
                .orElseThrow(() -> new ResourceNotFoundException("CAN'T DELETE USER INFORMATION ID: " +
                        userInfo.getId() + " NOT EXISTING"));
        userInfoRepository.deleteById(userInfo.getId());
    }
}
