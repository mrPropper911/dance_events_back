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
    @Autowired
    private RoleRepositoryJpa roleRepositoryJpa;


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

    @Sql(scripts = {"/sql/clearDatabase.sql"})
    @Test
    public void findRoleByTitle_withExistingRoleFromSave_shouldProperlyFindRole() {
        //given
        String ROLE_TITLE_FOR_SEARCH = "Administrator-Test";
        Role newRole = new Role();
        newRole.setRoleTitle(ROLE_TITLE_FOR_SEARCH);
        Role expectedRole = roleRepository.save(newRole);
        assertThat(expectedRole).isNotNull();
        //when
        Optional<Role> actualRole = roleRepositoryJpa.findByRoleTitle(ROLE_TITLE_FOR_SEARCH);
        //then
        assertThat(actualRole).isPresent();
    }

    @Sql(scripts = {"/sql/clearDatabase.sql", "/sql/addRolesForUsers.sql"})
    @Test
    public void findRoleByTitle_withExistingRole_shouldProperlyFindRole(){
        //given
        String ROLE_TITLE_FOR_SEARCH = "Administrator";
        Optional<Role> actualRole = roleRepositoryJpa.findByRoleTitle(ROLE_TITLE_FOR_SEARCH);
        //then
        assertThat(actualRole).isPresent();
    }

    @Sql(scripts = {"/sql/clearDatabase.sql", "/sql/addRolesForUsers.sql"})
    @Test
    public void findAll_withExistingRole_shouldProperlyFindAllRole (){
        //given
        int EXPECTED_SIZE_OF_ROLE = 3;
        Iterable<Role> actualRole = roleRepository.findAll();
        //then
        assertThat(actualRole).hasSize(EXPECTED_SIZE_OF_ROLE);
    }


}