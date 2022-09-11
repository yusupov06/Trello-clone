package uz.md.trello.dtos.board;

import lombok.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Fri 02/09/22 09:03
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class BoardReorderRequest {
    private UUID boardId;
    private Integer position;
}
