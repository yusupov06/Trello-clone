package uz.md.trello.domains.list;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import uz.md.trello.domains.Domain;
import uz.md.trello.domains.workspace.WorkspaceUser;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Wed 24/08/22 16:04
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity

public class Comment implements Domain {

    public Comment(Comment that){
        this(UUID.randomUUID(), that.getOwner(),
                that.getRepliedUsername(),
                LocalDateTime.now(), that.getBody(),
                null, false);
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

    @OneToOne(cascade = CascadeType.ALL)
    private WorkspaceUser owner;

    private String repliedUsername;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private String body;
    @ManyToOne
    private Task task;

    @Builder.Default
    @Column(columnDefinition = "bool default false")
    private boolean deleted = false;

}
