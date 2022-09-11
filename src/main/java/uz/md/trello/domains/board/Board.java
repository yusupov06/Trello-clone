package uz.md.trello.domains.board;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.repository.Query;
import uz.md.trello.domains.Domain;
import uz.md.trello.domains.list.BoardList;
import uz.md.trello.domains.list.Label;
import uz.md.trello.domains.upload.Upload;
import uz.md.trello.domains.workspace.Workspace;
import uz.md.trello.domains.workspace.WorkspaceUser;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Wed 24/08/22 15:48
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Board implements Domain {

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

    private String boardTitle;
    private String description;
    @OneToOne
    private WorkspaceUser owner;
    @ManyToOne
    private Workspace workspace;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board")
    @Builder.Default
    @OrderBy("listOrder")
    private List<BoardList> lists = new ArrayList<>();

    @OneToOne
    private Upload cover;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Builder.Default
    private boolean deleted = false;

    @Builder.Default
    private boolean archived = false;

    @Enumerated(EnumType.STRING)
    private BoardVisibility visibility;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "board")
    private List<BoardUser> members = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "board")
    private List<Label> labels = new ArrayList<>();
}
