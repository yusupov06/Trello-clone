package uz.md.trello.dtos.label;

import lombok.*;
import uz.md.trello.dtos.DTO;
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

public class LabelUDTO implements DTO {

    Long id;
    private String name;
    private String color;
    private UUID boardId;

}
