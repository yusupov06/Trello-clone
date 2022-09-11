package uz.md.trello.dtos.board.user;

import lombok.*;
import uz.md.trello.domains.board.BoardUserRole;
import uz.md.trello.domains.workspace.WorkspaceUser;
import uz.md.trello.dtos.workspace.WorkspaceUserDTO;

import javax.persistence.*;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Fri 26/08/22 10:24
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class BoardUserDTO {

    private UUID id;

    private WorkspaceUserDTO user;

    private BoardUserRole role;

}
