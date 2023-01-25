package by.belyahovich.dance_events.repository.role;

import by.belyahovich.dance_events.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepositoryJpa extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleTitle (String role);
}
