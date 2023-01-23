package by.belyahovich.dance_events.service.role.implementation;

import by.belyahovich.dance_events.domain.Role;
import by.belyahovich.dance_events.repository.role.RoleRepository;
import by.belyahovich.dance_events.service.role.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("Role service unit-test")
class RoleServiceImplTest {

    protected Role role = new Role();
    private RoleRepository roleRepository;
    private RoleService roleService;

    @BeforeEach
    public void init() {
        roleRepository = Mockito.mock(RoleRepository.class);
        roleService = new RoleServiceImpl(roleRepository);

        role.setId(1L);
        role.setRoleTitle("SOME_TITLE");
    }

    @Test
    void createRole_withNotExistingRole_shouldProperlyCreateNewRole() {
        //when
        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenReturn(role);
        //then
        Role actualRole = roleService.createRole(role);
        assertThat(actualRole).isNotNull();
        assertThat(actualRole).isEqualTo(role);
    }

    @Test
    void deleteRole() {
        //when
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));
        //then
        roleService.deleteRole(role);
        verify(roleRepository, times(1)).delete(role);
    }
}