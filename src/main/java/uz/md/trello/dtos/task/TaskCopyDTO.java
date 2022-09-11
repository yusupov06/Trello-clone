package uz.md.trello.dtos.task;

import lombok.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Mon 29/08/22 14:03
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString

public class TaskCopyDTO {

    private UUID taskId;
    private String title;
    private boolean keepLabels;
    private UUID listId;
    private Integer position;

}
