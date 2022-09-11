package uz.md.trello.dtos.workspace;

import lombok.*;
import uz.md.trello.domains.auth.AuthUser;
import uz.md.trello.domains.workspace.WorkspaceType;
import uz.md.trello.dtos.DTO;
import uz.md.trello.dtos.board.BoardDTO;

import java.util.List;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Wed 24/08/22 19:03
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter

public class WorkspaceUDTO implements DTO {

    private UUID id;

    private String name;

    private WorkspaceType type;

    private String description;


}
