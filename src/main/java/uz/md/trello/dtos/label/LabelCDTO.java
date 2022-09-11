package uz.md.trello.dtos.label;

import lombok.*;
import uz.md.trello.domains.board.Board;
import uz.md.trello.dtos.DTO;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Mon 29/08/22 15:49
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class LabelCDTO implements DTO {
    private String name;
    private String color;
    private UUID boardId;
}
