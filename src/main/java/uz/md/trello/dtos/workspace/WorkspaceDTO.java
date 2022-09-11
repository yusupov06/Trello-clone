package uz.md.trello.dtos.workspace;

import lombok.*;
import uz.md.trello.domains.auth.AuthUser;
import uz.md.trello.domains.board.Board;
import uz.md.trello.domains.workspace.WorkspaceType;
import uz.md.trello.domains.workspace.WorkspaceUser;
import uz.md.trello.dtos.DTO;
import uz.md.trello.dtos.board.BoardDTO;
import uz.md.trello.dtos.board.BoardSentDTO;

import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Thu 25/08/22 08:40
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class WorkspaceDTO implements DTO {

    private UUID id;

    private String name;

    private WorkspaceType type;

    private String description;

}
