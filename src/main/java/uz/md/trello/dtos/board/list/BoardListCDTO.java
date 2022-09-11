package uz.md.trello.dtos.board.list;

import lombok.*;
import uz.md.trello.domains.board.Board;
import uz.md.trello.dtos.DTO;

import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Fri 26/08/22 10:45
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class BoardListCDTO implements DTO {

    private String name;
    private UUID boardId;

}
