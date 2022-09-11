package uz.md.trello.dtos.workspace;

import lombok.*;
import uz.md.trello.domains.auth.AuthUser;
import uz.md.trello.domains.workspace.Role;
import uz.md.trello.dtos.auth.AuthUserDTO;

import javax.persistence.*;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Fri 26/08/22 10:25
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class WorkspaceUserDTO {


    private UUID id;

    private AuthUserDTO user;

    private Role role;
}
