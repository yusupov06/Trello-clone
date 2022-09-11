package uz.md.trello.dtos.auth;

import lombok.*;
import uz.md.trello.dtos.DTO;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Thu 25/08/22 15:25
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class AuthRoleCDTO implements DTO {
    private String code;
    private String name;
}
