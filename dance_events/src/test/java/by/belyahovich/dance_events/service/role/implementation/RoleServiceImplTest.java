package by.belyahovich.dance_events.service.role.implementation;

import by.belyahovich.dance_events.config.ResourceNotFoundException;
import by.belyahovich.dance_events.domain.Role;
import by.belyahovich.dance_events.repository.role.RoleRepository;
import by.belyahovich.dance_events.repository.role.RoleRepositoryJpa;
import by.belyahovich.dance_events.service.role.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("RoleService unit-test")
class RoleServiceImplTest {

    private RoleRepository roleRepository;

    private RoleRepositoryJpa roleRepositoryJpa;
    private RoleService roleService;

    @BeforeEach
    public void init() {
        roleRepository = Mockito.mock(RoleRepository.class);
        roleRepositoryJpa = Mockito.mock(RoleRepositoryJpa.class);
        roleService = new RoleServiceImpl(roleRepository, roleRepositoryJpa);
    }

    @Test
    void createRole_withNotExistingRole_shouldProperlyCreateNewRole() {
        //given
        Role role = new Role(1L, "ROLE_TRAP");
        //when
        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());
        roleService.createRole(role);
        //then
        ArgumentCaptor<Role> argumentCaptor =
                ArgumentCaptor.forClass(Role.class);
        verify(roleRepository).save(argumentCaptor.capture());
        Role captureRole = argumentCaptor.getValue();
        assertThat(captureRole).isEqualTo(role);
    }

    @Test
    void createRole_withExistingRole_shouldThrowException() {
        //given
        Role role = new Role(1L, "ROLE_TRAP");
        //when
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));
        //then
        assertThatThrownBy(() -> roleService.createRole(role))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("THIS ROLE WITH TITLE: ROLE_TRAP ALREADY EXISTS");
    }

    @Test
    void deleteRole_withExistingRole_shouldProperlyDeleteRole() {
        //given
        Role role = new Role(1L, "ROLE_TRAP");
        //when
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));
        roleService.deleteRole(role);
        //then
        ArgumentCaptor<Role> argumentCaptor =
                ArgumentCaptor.forClass(Role.class);
        verify(roleRepository).delete(argumentCaptor.capture());
        Role captureRole = argumentCaptor.getValue();
        assertThat(captureRole).isEqualTo(role);
    }

    @Test
    void deleteRole_withNotExistingRole_shouldThrowException() {
        //given
        Role role = new Role(1L, "ROLE_TRAP");
        //when
        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> roleService.deleteRole(role))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("THIS ROLE WITH TITLE: ROLE_TRAP NOT EXISTS");
    }

    @Test
    void findRoleByTitle_wihExistingRole_shouldProperlyFindRole() {
        //given
        Role role = new Role(1L, "ROLE_TRAP");
        //when
        when(roleRepositoryJpa.findByRoleTitle(anyString())).thenReturn(Optional.of(role));
        //then
        Optional<Role> actualRole = roleService.findRoleByTitle("ROLE_TRAP");
        assertThat(actualRole).isPresent();
        assertThat(actualRole).isEqualTo(Optional.of(role));
    }

    @Test
    void findRoleByTitle_wihNotExistingRole_shouldThrowException() {
        //when
        when(roleRepositoryJpa.findByRoleTitle(anyString())).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> roleService.findRoleByTitle("ROLE_ANY"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("THIS ROLE WITH TITLE: ROLE_ANY NOT EXISTS");
    }

    @Test
    void findAllRole_withExistingRole_shouldProperlyFindAllRole() {
        //given
        Role role_1 = new Role(1L, "ROLE_TRAP1");
        Role role_2 = new Role(2L, "ROLE_TRAP2");
        Set<Role> expectedRoleSet = Set.of(role_1, role_2);
        //when
        when(roleRepository.findAll()).thenReturn(expectedRoleSet);
        Set<String> actualAllRole = roleService.findAllRole();
        //then
        assertThat(actualAllRole).isNotEmpty();
        assertThat(actualAllRole).hasSize(expectedRoleSet.size());
    }
}