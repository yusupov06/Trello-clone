package uz.md.trello.domains.workspace;

import lombok.*;
import uz.md.trello.domains.board.Board;

import javax.persistence.*;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Wed 24/08/22 16:45
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String body;

    @OneToOne
    private Workspace workspace;

    @OneToOne
    private Board board;

    @ManyToOne
    private WorkspaceUser owner;



}
