package by.belyahovich.dance_events.repository.role;

import by.belyahovich.dance_events.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepositoryJpa extends JpaRepository<Role, Long> {

    /**
     * <h2>Find role by role title</h2>
     *
     * @param roleTitle title of {@link Role}
     * @return {@link Role}
     */
    Optional<Role> findByRoleTitle (String roleTitle);
}
