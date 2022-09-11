package uz.md.trello.domains.list;

import lombok.*;
import uz.md.trello.domains.Domain;
import uz.md.trello.domains.board.Board;

import javax.persistence.*;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Mon 29/08/22 14:15
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity

public class Label implements Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String name;
    private String color;
    @ManyToOne
    private Board board;

}
