package uz.md.trello.dtos.board.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.md.trello.dtos.workspace.WorkspaceUserDTO;

import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Sat 03/09/22 16:38
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class BoardUserSimpDTO {
    private UUID id;
    private String name;
}
