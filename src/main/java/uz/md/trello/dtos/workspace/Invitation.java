package uz.md.trello.dtos.workspace;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import uz.md.trello.domains.auth.AuthUser;
import uz.md.trello.domains.workspace.Workspace;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Thu 25/08/22 22:32
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity

public class Invitation {

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
    private AuthUser sender;

    @OneToOne
    private AuthUser receiver;

    private String invitationText;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne
    private Workspace workspace;

}
