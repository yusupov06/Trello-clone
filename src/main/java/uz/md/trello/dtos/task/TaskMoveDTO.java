package uz.md.trello.dtos.task;

import lombok.*;

import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Mon 29/08/22 14:37
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class TaskMoveDTO {

    private UUID taskId;
    private UUID listId;
    private Integer position;

}

