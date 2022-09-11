package uz.md.trello.domains.workspace;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import uz.md.trello.domains.Domain;
import uz.md.trello.domains.auth.AuthUser;
import uz.md.trello.domains.board.Board;

import javax.persistence.*;
import java.util.*;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Wed 24/08/22 15:27
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@Entity
@ToString
public class Workspace implements Domain {

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

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private WorkspaceUser owner;
    @Enumerated(EnumType.STRING)
    private WorkspaceType type;

    private String description;

    @Column(columnDefinition = "bool default false")
    private boolean deleted = false;

    public void setType(String type) {
        WorkspaceType workspaceType = WorkspaceType.OTHER;
        for (WorkspaceType value : WorkspaceType.values()) {
            if (value.name().equals(type)){
                workspaceType = value;
            }
        }
        this.type = workspaceType;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "workspace", cascade = CascadeType.ALL)
    private Collection<Board> boards;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "workspace")
    @Builder.Default
    Set<WorkspaceUser> members = new HashSet<>();



}