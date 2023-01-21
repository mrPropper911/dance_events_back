package by.belyahovich.dance_events.repository.userinfo;

import by.belyahovich.dance_events.domain.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {
}
