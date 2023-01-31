package by.belyahovich.dance_events.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "roleTitle")
    private String roleTitle;

    @OneToMany(mappedBy = "role")
    private Set<User> usersOfRole;

    public Role(long id, String roleTitle) {
        this.id = id;
        this.roleTitle = roleTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        if (id != role.id) return false;
        if (!Objects.equals(roleTitle, role.roleTitle)) return false;
        return Objects.equals(usersOfRole, role.usersOfRole);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (roleTitle != null ? roleTitle.hashCode() : 0);
        result = 31 * result + (usersOfRole != null ? usersOfRole.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleTitle='" + roleTitle + '\'' +
                ", usersOfRole=" + usersOfRole +
                '}';
    }
}
