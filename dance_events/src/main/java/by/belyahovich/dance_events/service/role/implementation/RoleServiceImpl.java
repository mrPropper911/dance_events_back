package by.belyahovich.dance_events.service.role.implementation;

import by.belyahovich.dance_events.config.ResourceNotFoundException;
import by.belyahovich.dance_events.domain.Role;
import by.belyahovich.dance_events.repository.role.RoleRepository;
import by.belyahovich.dance_events.repository.role.RoleRepositoryJpa;
import by.belyahovich.dance_events.service.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleRepositoryJpa roleRepositoryJpa;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, RoleRepositoryJpa roleRepositoryJpa) {
        this.roleRepository = roleRepository;
        this.roleRepositoryJpa = roleRepositoryJpa;
    }

    @Override
    public Role createRole(Role role) {
        Optional<Role> actualRole = roleRepository.findById(role.getId());
        if (actualRole.isPresent()) {
            throw new ResourceNotFoundException("THIS ROLE WITH TITLE: " + role.getRoleTitle() + " ALREADY EXISTS");
        }
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Role role) {
        Optional<Role> actualRole = roleRepository.findById(role.getId());
        if (actualRole.isEmpty()) {
            throw new ResourceNotFoundException("THIS ROLE WITH TITLE: " + role.getRoleTitle() + " NOT EXISTS");
        }
        roleRepository.delete(role);
    }

    @Override
    public Optional<Role> findRoleByTitle(String titleRole) {
        Optional<Role> roleByRoleTitle = roleRepositoryJpa.findByRoleTitle(titleRole);
        if (roleByRoleTitle.isEmpty()) {
            throw new ResourceNotFoundException("THIS ROLE WITH TITLE: " + titleRole + " NOT EXISTS");
        }
        return roleByRoleTitle;
    }

    @Override
    public Set<Role> findAllRole() {
        Set<Role> returnAllRole = new HashSet<>();
        Iterable<Role> all = roleRepository.findAll();
        for (Role iter : all) {
            returnAllRole.add(new Role(iter.getId(), iter.getRoleTitle()));
        }
        if (returnAllRole.isEmpty()) {
            throw new ResourceNotFoundException("NO ROLE EXISTS");
        }
        return returnAllRole;
    }
}
