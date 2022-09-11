package uz.md.trello.domains.list;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import uz.md.trello.domains.Domain;
import uz.md.trello.domains.board.Board;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Wed 24/08/22 15:49
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class BoardList implements Domain{


    public BoardList(BoardList that){

        this(UUID.randomUUID(),that.getName(), that.getListOrder(),  that.getArchivedAt(), LocalDateTime.now(), that.getUpdatedAt(),
                that.getBoard(), that.isDeleted(), that.isArchived(), new ArrayList<>());

        List<Task> tasks1 = new ArrayList<>();
        for (Task task : that.getTasks()) {
            tasks1.add(new Task(task));
        }
        this.tasks = tasks1;
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

//    @Column(unique = true)
    private int listOrder;

    private LocalDateTime archivedAt;

    @CreationTimestamp
    @Column(columnDefinition = "timestamp default now()")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    private Board board;

    @Builder.Default
    @Column(columnDefinition = "bool default false")
    private boolean deleted = false;

    @Builder.Default
    @Column(columnDefinition = "bool default false")
    private boolean archived = false;

    @OneToMany(mappedBy = "boardList")
    @OrderBy("position")
    private List<Task> tasks  = new ArrayList<>();

}
