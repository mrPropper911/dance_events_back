package by.belyahovich.dance_events.repository.user;

import by.belyahovich.dance_events.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
