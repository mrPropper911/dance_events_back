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

    /**
     * <h2>Find all liked users events</h2>
     *
     * @param id {@link User} id
     * @return list of liked user {@link Event}
     */
    @Modifying
    @Transactional
    @Query("SELECT e FROM Event e JOIN e.likedByUser u where u.id = :id")
    List<Event> getAllLikedUserEventsByUserId(@Param("id") Long id);

    /**
     * <h2>Find user by login for validation</h2>
     *
     * @param login {@link User} login
     * @return {@link User}
     */
    Optional<User> findUserByLogin (String login);

    /**
     * <h2>Update active field by user login</h2>
     *
     * @param login {@link User} login
     * @param active {@link User} activity (false to inactive)
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE User u SET u.active = :active WHERE u.login = :login")
    void updateUserActive( @Param("login")String login, @Param("active") boolean active);


}
