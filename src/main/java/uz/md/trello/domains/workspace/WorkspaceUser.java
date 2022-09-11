package uz.md.trello.domains.workspace;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import uz.md.trello.domains.auth.AuthUser;
import uz.md.trello.dtos.auth.AuthUserDTO;

import javax.persistence.*;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Wed 24/08/22 15:45
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@ToString
public class WorkspaceUser {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToOne
    private AuthUser user;
    @ManyToOne
    private Workspace workspace;

    @Enumerated(EnumType.STRING)
    private Role role;

}
