package by.belyahovich.dance_events.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
}
