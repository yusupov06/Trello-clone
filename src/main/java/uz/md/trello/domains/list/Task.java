package uz.md.trello.domains.list;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import uz.md.trello.domains.Domain;
import uz.md.trello.domains.board.BoardUser;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Wed 24/08/22 16:51
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@ToString

public class Task implements Domain {

    public static Integer currentPosition = 1;

    public Task(Task that){
        this(UUID.randomUUID(), that.getName(),that.getDescription(),
                new ArrayList<>(), that.getBoardList(), LocalDateTime.now(),
                null, that.getPosition(), false, false,
                null, that.getLabels(), that.getDeadline());
        List<Comment> comments1 =new ArrayList<>();

        for (Comment comment : that.getComments()) {
            comments1.add(new Comment(comment));
        }
        this.comments = comments1;
        this.members = new ArrayList<>(that.getMembers());
    }
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

    private String description;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "task_member",
            joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "board_user_id", referencedColumnName = "id"))
    @Builder.Default
    private Collection<BoardUser> members = new ArrayList<>();

    @ManyToOne
    private BoardList boardList;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

//    @Column(unique = true)
    private Integer position;

    @Builder.Default
    @Column(columnDefinition = "bool default false")
    private boolean deleted = false;

    @Builder.Default
    @Column(columnDefinition = "bool default false")
    private boolean archived = false;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "task")
    @Builder.Default
    @OrderBy("id")
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "task_label",
            joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "label_id", referencedColumnName = "id"))
    @Builder.Default
    private List<Label> labels = new ArrayList<>();

    private LocalDateTime deadline;

}
