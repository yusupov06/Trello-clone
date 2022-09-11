package uz.md.trello.dtos.board.list;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Sun 28/08/22 11:25
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class BoardListMoveDTO {

    private UUID boardListId;
    private UUID boardId;
    private Integer position;

}
