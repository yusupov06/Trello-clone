package uz.md.trello.dtos.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Sat 03/09/22 18:10
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class TaskSimpDTO {

    private UUID id;
    private String name;
    private Integer position;
    private int commentsCount;

}
