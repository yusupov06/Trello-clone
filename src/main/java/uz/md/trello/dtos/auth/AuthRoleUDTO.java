package uz.md.trello.dtos.auth;

import lombok.*;
import uz.md.trello.dtos.DTO;

import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Thu 25/08/22 15:27
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class AuthRoleUDTO implements DTO {

    private Long id;
    private String code;
    private String name;

}
