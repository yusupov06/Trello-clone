package uz.md.trello.dtos.board.list;

import lombok.*;
import uz.md.trello.dtos.DTO;
import uz.md.trello.dtos.task.TaskSimpDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Fri 26/08/22 10:28
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class BoardListDTO implements DTO {

    private UUID id;
    private String name;
    private int listOrder;
    private List<TaskSimpDTO> tasks  = new ArrayList<>();
}
