package uz.md.trello.dtos.board.list;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Sun 28/08/22 10:29
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BoardListCopyDTO {
    private UUID id;
    private String newName;
}
