package uz.md.trello.dtos.task;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.md.trello.domains.list.BoardList;
import uz.md.trello.dtos.DTO;

import java.time.LocalDateTime;
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

public class TaskCDTO implements DTO {
    private String name;
    private String description;
    private UUID boardListId;
    private LocalDateTime deadline;
}
