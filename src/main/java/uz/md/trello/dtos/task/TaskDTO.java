package uz.md.trello.dtos.task;

import lombok.*;
import uz.md.trello.dtos.DTO;
import uz.md.trello.dtos.board.user.BoardUserSimpDTO;
import uz.md.trello.dtos.label.LabelDTO;
import uz.md.trello.dtos.task.comment.CommentDTO;

import java.util.List;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Sun 28/08/22 12:03
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class TaskDTO implements DTO {

    private UUID id;
    private List<BoardUserSimpDTO> members;
    private List<LabelDTO> labels;
    private Integer position;
    private String name;
    private List<CommentDTO> comments;

}
