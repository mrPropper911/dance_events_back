package by.belyahovich.dance_events.repository.role;

import by.belyahovich.dance_events.domain.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Role repository module test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Sql(scripts = {"/sql/clearDatabase.sql"})
    @Test
    public void save_withNotExistingRole_shouldProperlySaveNewRole() {
        //given
        String TITLE_OF_NEW_ROLE = "Sheaf";
        Role newRole = new Role();
        newRole.setRoleTitle(TITLE_OF_NEW_ROLE);
        Role expectedRoleToDB = roleRepository.save(newRole);
        assertThat(expectedRoleToDB).isNotNull();
        //when
        Optional<Role> actualRole = roleRepository.findById(expectedRoleToDB.getId());
        //then
        assertThat(actualRole).isPresent();
        assertThat(actualRole.get().getRoleTitle()).isEqualTo(TITLE_OF_NEW_ROLE);
    }
}