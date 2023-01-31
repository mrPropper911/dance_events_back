package by.belyahovich.dance_events.repository.user;

import by.belyahovich.dance_events.domain.Event;
import by.belyahovich.dance_events.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepositoryJpa extends JpaRepository<User, Long> {

    //Find all liked users events
    @Modifying
    @Transactional
    @Query("SELECT e FROM Event e JOIN e.likedByUser u where u.login = :login")
    List<Event> getAllLikedUserEventsByUserLogin(@Param("login") String login);

    //Find user by login for validation
    Optional<User> findUserByLogin (String login);

    //Update active field
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE User u SET u.active = :active WHERE u.login = :login")
    void updateUserActive( @Param("login")String login, @Param("active") boolean active);


}
