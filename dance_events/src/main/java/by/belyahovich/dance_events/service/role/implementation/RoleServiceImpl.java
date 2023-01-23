package by.belyahovich.dance_events.service.role.implementation;

import by.belyahovich.dance_events.config.ResourceNotFoundException;
import by.belyahovich.dance_events.domain.Role;
import by.belyahovich.dance_events.repository.role.RoleRepository;
import by.belyahovich.dance_events.service.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role createRole(Role role) {
        Optional<Role> actualRole = roleRepository.findById(role.getId());
        if (actualRole.isPresent()){
            throw new ResourceNotFoundException("THIS ROLE WITH TITLE: " + role.getRoleTitle() + " ALREADY EXISTS");
        }
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Role role) {
        Optional<Role> actualRole = roleRepository.findById(role.getId());
        if (actualRole.isEmpty()){
            throw new ResourceNotFoundException("THIS ROLE WITH TITLE: " + role.getRoleTitle() + " NOT EXISTS");
        }
        roleRepository.delete(role);
    }
}
