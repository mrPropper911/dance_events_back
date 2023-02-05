package by.belyahovich.dance_events.service.role.implementation;

import by.belyahovich.dance_events.domain.Role;
import by.belyahovich.dance_events.repository.role.RoleRepository;
import by.belyahovich.dance_events.repository.role.RoleRepositoryJpa;
import by.belyahovich.dance_events.service.role.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("Role service unit-test")
class RoleServiceImplTest {

    protected Role role = new Role();
    protected Role role2 = new Role();
    private RoleRepository roleRepository;
    private RoleRepositoryJpa roleRepositoryJpa;
    private RoleService roleService;

    @BeforeEach
    public void init() {
        roleRepository = Mockito.mock(RoleRepository.class);
        roleRepositoryJpa = Mockito.mock(RoleRepositoryJpa.class);
        roleService = new RoleServiceImpl(roleRepository, roleRepositoryJpa);

        role.setId(1L);
        role.setRoleTitle("SOME_TITLE");
        role2.setId(2L);
        role2.setRoleTitle("SOME_TITLE_2");
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
    void deleteRole_withExistingRole_shouldProperlyDeleteRole() {
        //when
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));
        //then
        roleService.deleteRole(role);
        verify(roleRepository, times(1)).delete(role);
    }

    @Test
    void findRoleByTitle_wihExistingRole_shouldProperlyFindRole() {
        //when
        when(roleRepositoryJpa.findByRoleTitle(anyString())).thenReturn(Optional.of(role));
        //then
        Optional<Role> actualRole = roleService.findRoleByTitle("SOME_TITLE");
        assertThat(actualRole).isPresent();
        assertThat(actualRole).isEqualTo(Optional.of(role));
    }

//    @Test
//    void findAllRole_withExistingRole_shouldProperlyFindAllRole() {
//        //when
//        Set<Role> expectedSetRole = new HashSet<>(Arrays.asList(role, role2));
//        when(roleRepository.findAll()).thenReturn(expectedSetRole);
//        //then
//        Set<Role> actualAllRole = roleService.findAllRole();
//        assertThat(actualAllRole).hasSize(expectedSetRole.size());
//    }
}