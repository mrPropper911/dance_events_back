package by.belyahovich.dance_events.repository.role;

import by.belyahovich.dance_events.domain.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
}
