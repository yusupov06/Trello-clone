package uz.md.trello.dtos.task.comment;

import lombok.*;
import uz.md.trello.dtos.DTO;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Fri 02/09/22 15:15
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class CommentCDTO implements DTO {
    private String repliedUsername;
    private String body;
    private UUID taskId;
}
