package uz.md.trello.dtos.label;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.md.trello.dtos.DTO;


/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Mon 29/08/22 15:49
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class LabelDTO implements DTO {
    private Long id;
    private String name;
    private String color;
}
