package uz.md.trello.dtos.board.user;

import uz.md.trello.domains.board.BoardUserRole;
import uz.md.trello.domains.workspace.WorkspaceUser;

import javax.persistence.*;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Fri 26/08/22 10:39
 */
public class BoardUserCDTO {

    private Long workspaceUserId;

    private BoardUserRole role;

}
