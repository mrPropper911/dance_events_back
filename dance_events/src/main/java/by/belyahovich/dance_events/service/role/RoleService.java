package by.belyahovich.dance_events.service.role;

import by.belyahovich.dance_events.domain.Role;

import java.util.Optional;
import java.util.Set;

public interface RoleService {

    Role createRole(Role role);

    void deleteRole(Role role);

    Optional<Role> findRoleByTitle(String titleRole);

    Set<Role> findAllRole();

}
