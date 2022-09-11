package uz.md.trello.dtos.task.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.md.trello.domains.list.Task;
import uz.md.trello.domains.workspace.WorkspaceUser;
import uz.md.trello.dtos.DTO;
import uz.md.trello.dtos.workspace.WorkspaceUserDTO;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Sun 28/08/22 12:12
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class CommentDTO implements DTO {

    private UUID id;
    private String fullName;
    private UUID ownerId;
    private String repliedUsername;
    private String body;
}
